package com.example.myapplication.mainPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.addPage.*

class BannerSlideFragment(var bannerImage : Int, var pos : Int) : Fragment() {

    //버튼 id는 fragment_banner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var View= inflater.inflate(R.layout.banner_fragment, container, false)
        //bind(View)

        View.setBackgroundResource(bannerImage)

        return View

    }

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    /*
    fun bind(itemView:View):View?{
        var banner_insert= itemView.findViewById<Button>(R.id.fragment_banner);
        banner_insert.setBackgroundResource(bannerImage)
        banner_insert.setOnClickListener{
            Log.d("종료지점확인", "BannerSlideFragment 버튼 클릭")
            //프래그먼트상에 context 존재하지 않기 때문에 getActivity() 사용
            //Add_page 호출한다 putExtra로 몇 번째 페이지인지도 보내준다
            val intent :Intent
            intent=Intent(getActivity(), Add_page::class.java)

            if (pos<=12) intent.putExtra("pos", pos)
            else intent.putExtra("pos", 0)

            //intent=Intent(getActivity(), Add_page::class.java)
            startActivity(intent)
        }
        return itemView

    }*/


}