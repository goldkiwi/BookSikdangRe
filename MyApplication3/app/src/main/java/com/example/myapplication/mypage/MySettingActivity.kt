package com.example.myapplication.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView

class MySettingActivity : AppCompatActivity() {
    private var isLoading = false
    private val GALLERY_CODE = 1001
    private val MIME_TYPES = arrayListOf("image/jpeg", "image/png")

    lateinit var close : ImageView
    lateinit var done : ImageView
    lateinit var loading : ProgressBar

    lateinit var user_image : CircleImageView
    lateinit var from_folder : ImageView
    lateinit var from_camera : ImageView

    lateinit var edit_name : EditText
    lateinit var edit_username : EditText
    lateinit var edit_email : EditText
    lateinit var edit_phone_number : EditText

    var selected_image_url : Uri? = null

    lateinit var storageReference: StorageReference
    lateinit var uploadTask: UploadTask

    var myUser : User? = null
    val requestOptions : RequestOptions by lazy {
        RequestOptions()
            .placeholder(R.drawable.profile_placeholder)
            .transforms(CenterCrop())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_setting)
        init_Ui()
        init_data()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            selected_image_url = data.data
            set_profile_image(selected_image_url.toString())
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK) {
            var result = CropImage.getActivityResult(data)
            selected_image_url = result.uri
            if(selected_image_url == null) return
            set_profile_image(selected_image_url.toString())
        } else  {
            Toast.makeText(this, "죄송합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init_data() {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    myUser = snapshot.getValue(User::class.java)
                    if(myUser == null) finish()
                    lazyUiSetting()
                    loading.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("My Setting Activity", "${error}")
                }
            })

        storageReference = FirebaseStorage.getInstance().getReference("UserImages")
    }

    private fun init_Ui() {
        close = findViewById(R.id.close)
        close.setOnClickListener {
            finish()
        }

        done = findViewById(R.id.done)
        done.setOnClickListener {
            update_user()
        }

        user_image = findViewById(R.id.user_image)
        set_profile_image(null)

        from_folder = findViewById(R.id.from_folder)
        from_folder.setOnClickListener {
            var _intent = Intent(Intent.ACTION_PICK)
            _intent.setType("image/*")
            _intent.putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES)
            startActivityForResult(_intent, GALLERY_CODE)
        }

        from_camera = findViewById(R.id.from_camera)
        from_camera.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this)
        }

        edit_name = findViewById(R.id.edit_name)
        edit_username = findViewById(R.id.edit_username)
        edit_email = findViewById(R.id.edit_email)
        edit_phone_number = findViewById(R.id.edit_phone_number)

        loading = findViewById(R.id.loading)
        loading.visibility = View.VISIBLE
    }

    private fun lazyUiSetting() {
        string2Edit(edit_name, myUser!!.username)
        string2Edit(edit_email, myUser!!.email)
        string2Edit(edit_username, myUser!!.user_alias)
        string2Edit(edit_phone_number, myUser!!.phone_number)

        set_profile_image(myUser!!.user_image)
    }

    private fun string2Edit(edit : EditText, string : String?) {
        if(string == null || string == "null") return
        edit.setText(string!!)
    }

    private fun set_profile_image(url : String?) {
        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(user_image)
    }

    private fun update_user() {
        if(isLoading == true) {
            Toast.makeText(this, "업로드 중입니다. 기다리세요.", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        loading.visibility = View.VISIBLE

        if(selected_image_url != null) {
            val fileReference = storageReference.child(
                System.currentTimeMillis().toString() + "." + get_file_extension(selected_image_url!!)

            )

            uploadTask = fileReference.putFile(selected_image_url!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }

                Log.d("uploadImage", fileReference.downloadUrl.toString())
                return@continueWithTask fileReference.downloadUrl
            }.addOnCompleteListener { task ->
                val image_url = task.result.toString()
                val new_user = update_list(image_url)
                loadDB(new_user)
            }
        } else {
            val new_user = update_list(null)
            loadDB(new_user)
        }

    }

    private fun get_file_extension(uri : Uri) : String? {
        var mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun update_list(image_url: String?) : HashMap<String, Any> {
        val new_phone_number = edit_phone_number.text.toString()
        val new_alias = edit_username.text.toString()
        val new_name = edit_name.text.toString()
        val new_email = edit_email.text.toString()

        var new_user = hashMapOf<String, Any>()

        new_user.put("id", myUser!!.id)
        if(TextUtils.isEmpty(new_name) == false) {
            new_user.put("username", new_name)
        } else {
            new_user.put("username", myUser!!.username!!)
        }
        if(TextUtils.isEmpty(new_alias) == false)
            new_user.put("user_alias", new_alias)
        if(TextUtils.isEmpty(image_url) == false)
            new_user.put("user_image", image_url!!)
        else if (myUser!!.user_image != null)
            new_user.put("user_image", myUser!!.user_image!!)
        if(TextUtils.isEmpty(new_phone_number) == false)
            new_user.put("phone_number", new_phone_number)
        if(TextUtils.isEmpty(new_email) == false)
            new_user.put("email", new_email)
        return new_user
    }

    private fun loadDB(new_user: HashMap<String, Any>) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .setValue(new_user)
            .addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "죄송합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                runOnUiThread{
                    loading.visibility = View.INVISIBLE
                }
            }
    }
}