package com.example.sikdangbook_rest.Table

import java.io.Serializable

//locX 테이블의 가로 좌표
//locY 테이블의 세로 좌표
//lengX 테이블의 가로길이
//lengY 테이블의 세로길이
//maxP 이 테이블에 최대 몇명 예약 가능한지
//floor 테이블이 위치한 층
//isbooked 테이블이 예약되어있는가
//isCircle 테이블이 원형인가
class Table_res(var locX:Float, var locy:Float, var lengX:Int, var lengY:Int, var maxP:Int, var floor:Int, var isBooked:Boolean, var isCircle:Boolean): Serializable {
}