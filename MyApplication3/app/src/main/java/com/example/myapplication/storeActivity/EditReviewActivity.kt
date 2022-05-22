package com.example.myapplication.storeActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import java.lang.ref.Reference
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Semaphore

class EditReviewActivity : AppCompatActivity() {
    lateinit var close : ImageView
    lateinit var save : TextView
    lateinit var image: ImageView
    lateinit var from_folder : ImageView
    lateinit var from_camera : ImageView
    lateinit var comment : EditText


    lateinit var rv_rating : RecyclerView
    lateinit var ratingAdapter: RatingAdapter

    var isUpload : Boolean = false
    var complete : Boolean = false
    var semaphore = Semaphore(1)

    val FOLDER_CODE = 1111
    val MIME_TYPES = arrayListOf("image/jpeg", "image/png")

    lateinit var storeInfo : StoreInfo
    lateinit var storageReference: StorageReference
    lateinit var uploadTask: UploadTask
    var selected_image_url : Uri? = null
    lateinit var user_name : String
    lateinit var storeReference: DatabaseReference

    var isCached = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_review)

        init_Data()
        init_Ui()
    }

    private fun init_Data() {
        storeInfo = intent.getSerializableExtra("store_info") as StoreInfo
        storageReference = FirebaseStorage.getInstance().getReference("Reviews")
            .child(storeInfo.store_name!!)
        user_name = FirebaseAuth.getInstance().currentUser!!.displayName!!
        storeReference = FirebaseDatabase.getInstance().getReference("Review")
            .child(storeInfo.store_id!!)
    }

    private fun init_Ui() {
        close = findViewById(R.id.close)
        close.setOnClickListener {
            finish()
        }

        save = findViewById(R.id.save)
        save.setOnClickListener {
            uploadImage()
        }
        image = findViewById(R.id.upload_image)

        from_folder = findViewById(R.id.from_folder)
        from_folder.setOnClickListener {
            var _intent = Intent(Intent.ACTION_PICK)
            _intent.setType("image/*")
            _intent.putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES)
            startActivityForResult(_intent, FOLDER_CODE)
        }

        from_camera = findViewById(R.id.from_camera)
        from_camera.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this)
        }

        comment = findViewById(R.id.comment)

        rv_rating = findViewById(R.id.rv_rating)
        ratingAdapter = RatingAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_rating.adapter = ratingAdapter
        rv_rating.layoutManager = linearLayoutManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == FOLDER_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            selected_image_url = data.data
            image.setImageURI(selected_image_url)
            isUpload = true
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK) {
            var result = CropImage.getActivityResult(data)
            selected_image_url = result.uri
            if(selected_image_url == null) return
            image.setImageURI(selected_image_url)
            isUpload = true
        } else  {
            Toast.makeText(this, "죄송합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNoComment() : Boolean{
        return TextUtils.isEmpty(comment.text.toString())
    }

    private fun isNoRate() : Boolean {
        return ratingAdapter.getRating() == 0
    }

    private fun getFileExtension(uri : Uri) : String? {
        var mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        if(isNoComment()) {
            Toast.makeText(this, "코멘트를 적지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(isNoRate()) {
            Toast.makeText(this, "별점을 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(complete) {
            Toast.makeText(this, "업로드중입니다. 기다리세요", Toast.LENGTH_SHORT).show()
            return
        }

        semaphore.acquire()
        complete = true
        semaphore.release()

        val rating = ratingAdapter.getRating()

        if(isUpload) {
            if(selected_image_url == null) {
                Toast.makeText(this, "죄송합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                return
            }

            val fileReference = storageReference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(selected_image_url!!)
            )
            uploadTask = fileReference.putFile(selected_image_url!!)
            uploadTask.continueWithTask { task ->
                if(!task.isSuccessful) {
                    throw task.exception!!
                }

                Log.d("uploadImage", fileReference.downloadUrl.toString())
                return@continueWithTask fileReference.downloadUrl
            }.addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    val image = task.result
                    val imageUrl = image.toString()

                    writeComment(imageUrl, rating)
                    writeTransaction(imageUrl, rating)
                    finish()
                }
            }
        } else {
            val imageUrl = "NULL"
            writeComment(imageUrl, rating)
            writeTransaction(imageUrl, rating)
            finish()
        }
    }

    private fun writeTransaction(image: String, rating : Int) {
        var sem = Semaphore(1)
        while (!isCached) {
            FirebaseDatabase.getInstance().getReference("Review")
                .child(storeInfo.store_id!!)
                .runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val infoRef = currentData.child("info")
                    //Log.d("writeTransaction", "${infoRef}. ${infoRef.key}")
                    //Log.d("writeTransaction", "${ratingRef}, ${ratingRef.key}")

                    val review_data = infoRef.getValue(ReviewData::class.java)
                    if (review_data == null) {
                        //Log.d("writeTransaction", "Not cached")
                        return Transaction.abort()
                    }
                    if(isCached) return Transaction.abort()
                    sem.acquire()
                    isCached = true
                    sem.release()

                    Log.d("writeTransaction", "${review_data}")
                    Log.d("writeTransaction", "isCaced : ${isCached}")


                     val update = hashMapOf<String, Int>(
                        "cnt_owner" to review_data.cnt_owner!!,
                        "cnt_user" to (review_data.cnt_user!! + 1),
                        "rating" to (review_data.rating!! + rating)
                    )

                    infoRef.value = update
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                }
            })
        }
    }
    private fun writeComment(image : String, rating : Int) {
        val write_time = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val writed_comment = comment.text.toString()

        var review = hashMapOf<String, Any>(
            "image" to image,
            "user_name" to user_name,
            "comment" to writed_comment,
            "rating" to rating,
            "date" to write_time,
            "recomment" to "NULL"
        )

        storeReference.child("Comments").push().setValue(review)
    }
}