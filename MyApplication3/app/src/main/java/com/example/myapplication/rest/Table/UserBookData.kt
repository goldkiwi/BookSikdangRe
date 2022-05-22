package com.example.myapplication.rest.Table


//TableStateIsBookedDialog_res 에서 사용
//tableNum은 모든 층의 테이블을 정렬 했을 때의 순서
//그 테이블과 같이 예약된 모든 예약 정보를 불러온다.
class UserBookData(tableNum:Int) {
    var userName:String = ""
    var phonNum:String = ""
    var bookNumber:String =""//예약 번호
    var bookStartTime =""
    var bookEndTime = ""
    var tableAL=ArrayList<Table>()

    init{
        userName = "홍길동"
        phonNum="010-1234-5678"
        bookNumber = "10987654321"
        bookStartTime="1200"
        bookEndTime="2400"

        var menuAL1=ArrayList<Menu>()
        menuAL1.add(Menu("민트초코피자", 10000, 1))
        menuAL1.add(Menu("오이치킨", 15000, 3))
        menuAL1.add(Menu("하와이안피자", 7000, 1))
        menuAL1.add(Menu("솔의눈", 3500, 2))

        var table1 = Table(1, 2, menuAL1)


        var menuAL2=ArrayList<Menu>()
        menuAL2.add(Menu("민트초코피자", 10000, 2))
        menuAL2.add(Menu("오이치킨", 15000, 1))
        menuAL2.add(Menu("육회", 20000, 5))

        var table2 = Table(1, 3, menuAL2)

        var menuAL3=ArrayList<Menu>()
        menuAL3.add(Menu("민트초코피자", 10000, 2))
        menuAL3.add(Menu("육개장", 4000, 2))
        menuAL3.add(Menu("초코우유", 50000, 3))

        var table3 = Table(2, 1, menuAL2)

        tableAL.add(table1)
        tableAL.add(table2)
        tableAL.add(table3)

    }

    inner class Table(var tableFloor:Int, var tableNum:Int, var menuAL: ArrayList<Menu>)
    inner class Menu(var menuName:String, var price:Int, var num:Int)

}