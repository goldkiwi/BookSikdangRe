package com.example.myapplication.recommendation

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

//Parcelable 넣음으로서 putExtra로 넘겨줄 수 있게 한다
class MsgCat() : Parcelable {
    //val tagListMax = 20 //리스트 개수 = 태그 개수
    var orderMax = 0//우선순위 붙은 태그 갯수 = 최대우선순위
    val aaa=12314

    //private var catName="먹을거"

    //var tagOrderList = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    //var tagOrderList : ArrayList<Int> = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)


    private var tagList = arrayOf("소고기", "닭고기", "돼지고기",
            "매운맛", "달콤한맛", "구수한맛",
            "짠맛", "뜨거운것", "시원한것",
            "생선", "새우", "조개",
            "소주", "맥주", "막걸리",
            "데이트", "단체", "혼밥",
            "야채", "좌식", "오감자",
            "썬칩", "꼬북칩", "베라",
            "뫄이쩡", "졸려", "까까")
    val tagListMax = tagList.size //리스트 개수 = 태그 개수
    var orderText = "aaaaaa"
    var tagOrderList = Array(tagListMax, { 0 })
    var tagOrderListString=setTagOrderListStringToZero()
/*
    constructor(parcel: Parcel) : this(
        parcel.readInt()?:"",
        parcel.readString().toString()?:"",
        parcel.readString().toString()?:"",
        parcel.readArray(Int::class.java.classLoader) as ArrayList<Int>?:""
    )*/


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        //parcel.writeArray(tagOrderList)
        parcel.writeInt(orderMax)
        parcel.writeString(orderText)
        parcel.writeString(tagOrderListString)
        //parcel.writeList(tagOrderList)
    }




    constructor(parcel: Parcel) : this() {
        orderMax = parcel.readInt()
        orderText = parcel.readString().toString()
        tagOrderListString = parcel.readString().toString()
        //parcel.readArray(Int::class.java.classLoader) as ArrayList<Int>
        setStringToArray()
        //Log.d("확인 MsgCat constructor 복사 확인", orderText)
        logOrderList()

        //parcel.readList(tagOrderList, Int.javaClass.classLoader)
        //tagOrderList= parcel.readArray(Int.javaClass.classLoader) as Array<Int>
        //tagOrderList=parcel.readArray(Int::class.java)
        //tagOrderList=parcel.readIntArray(tagOrderList)

    }

    //parcelable 로 Array 넘기는데 문제가 있기 때문에 String으로 넘긴 뒤 Array를 String 결과에 맞춰 수정
    //String의 값을 수정하는 함수 , Array가 수정될 때마다 이 함수 호출해서 String을 수정한다
    //String에서 각 요소의 구분은 띄어쓰기로 구분해야할듯함
    //StringToArray 함수가 좀 복잡해질듯
    private fun setTagOrderListString(){
        var listString: String = ""

        for (i in 0 until tagListMax) {
            var listChar = tagOrderList[i].toString()
            listString += listChar
            listString += " "
        }
        tagOrderListString=listString
        //Log.d("확인 MsgCat setOrderListText() : List", listString)
    }
    //아무 태그도 선택하지 않고 오늘뭐먹지 누를 깨 tagOrderListString의 내용을 전부 0으로 set
    private fun setTagOrderListStringToZero(): String {
        var zeroTagOrderListString:String=""
        var i = 0
        while (i<tagListMax){
            zeroTagOrderListString+="0 "
            i++
        }
        return zeroTagOrderListString
    }

    //로그 남기는 함수 : 디버그용
    public fun logTagOrderListString(){
        Log.d("확인 MsgCat logTagOrderListString() : List", tagOrderListString)
    }
    public fun logTagOrderList(){
        var listString=""
        for (i in 0 until tagListMax) {
            var listChar = tagOrderList[i].toString()
            listString += listChar
            listString+=" "
        }
        Log.d("확인 MsgCat logTagOrderList() : List", listString)
    }
    //String 값 가지고 Array 수정하는 함수
    private fun setStringToArray(){

        var i = 0
        var j = 0
        var tempString = ""//여기에 두자리 또는 한 자리의 숫자 입시 저장
        var tempChar:String=""//여기에 한 자리의 숫자 저장되어 tempString에 합쳐짐
        while (true){
            Log.d("확인 MsgCat setStringToArray()", "종료지점 확인1")
            while (tempChar!= " "){
                Log.d("확인 MsgCat setStringToArray()", "종료지점 확인2"+" "+tempChar)
                tempChar = tagOrderListString[j].toString()
                Log.d("확인 MsgCat setStringToArray()", "종료지점 확인3")
                if (tempChar == " "){
                    j++
                    break
                }
                tempString+=tempChar
                j++
            }
            Log.d("확인 MsgCat setStringToArray()", "종료지점 확인4")
            tagOrderList[i] = tempString.toInt()
            Log.d("확인 MsgCat setStringToArray()", "종료지점 확인5")
            tempChar=""
            tempString=""

            i++
            if (i>=tagListMax) break
        }
        Log.d("확인 MsgCat setStringToArray()", "종료")
        logTagOrderList()
    }
    //태그 선택 또는 해제시 리스트 수정하는 함수
    public fun setListOn(catName: String) {
        Log.d("확인 MsgCat setListOn", catName)
        for (i in 0 until tagListMax) {//태그 리스트 한바퀴 돔
            if (catName == tagList[i]) if (tagOrderList[i] == 0) tagOrderList[i] = ++orderMax//catName을 태그리스트중에 찾고 그 태그가 몇번째인지 알게됐으니 그걸로 우선순위 확인하고 올림
        }
        setTagOrderListString()
        logOrderList()
    }

    public fun setListOff(catName: String) {
        Log.d("확인 MsgCat setListOff", catName)
        var catNum = 0
        for (i in 0 until tagListMax) if (catName == tagList[i]) catNum = i //catNum은 태그가 리스트의 몇 번째인지
        var catOrderNum = tagOrderList[catNum] //catOrderNum 은 우선순위 몇 번쨰인지
        listDown(catOrderNum)
        tagOrderList[catNum] = 0
        setTagOrderListString()
        logOrderList()
    }

    //디버그용 함수 각 태그와 그 우선순위를 orderText 에 집어넣음
    private fun setText() {
        var listString: String = ""
        for (i in 0 until tagListMax) {
            var listChar = tagOrderList[i].toString()
            listString += tagList[i]
            listString += listChar
        }
        orderText = listString
    }

    public fun getText(): String {
        setText()
        Log.d("확인 MsgCat getText : orderText, 아래는 logOrderList", orderText)
        logOrderList()

        return orderText
    }

    public fun logOrderList() {
        var listString: String = "orderMax = "
        listString += orderMax.toString()

        for (i in 0 until tagListMax) {
            var listChar = tagOrderList[i].toString()
            listString += tagList[i]
            listString += listChar
        }
    }


    private fun listDown(num: Int) {//받은 숫자보다 큰 숫자들은 모두 1씩 줄임

        if (orderMax == 0) return //우선순위 붙은 태그가 0개면 함수 종료
        for (i in 0 until tagListMax) if (tagOrderList[i] > num) tagOrderList[i]--
        orderMax--
    }

    public fun getTagList(): Array<String> {
        return tagList
    }






    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MsgCat> {
        override fun createFromParcel(parcel: Parcel): MsgCat {
            return MsgCat(parcel)
        }

        override fun newArray(size: Int): Array<MsgCat?> {
            return arrayOfNulls(size)
        }
    }
}

