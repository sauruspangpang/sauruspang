package com.ksj.sauruspang.Learnpackage

import com.ksj.sauruspang.R

data class QuizDay(
    val dayNumber: Int,
    val questions: List<QuizQuestion>
)
data class QuizQuestion(
    val imageId: Int,
    val korean: String,
    val english: String
)


sealed class QuizCategory(val name: String, val thumbnail: Int, val days: List<QuizDay>) {
    var currentDay: Int = 1

    object Colors : QuizCategory(
        "색",
        R.drawable.colors_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.colors_red, "빨강", "Red"),
                    QuizQuestion(R.drawable.colors_orange, "주황", "Orange"),
                    QuizQuestion(R.drawable.colors_yellow, "노랑", "Yellow"),
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.colors_green, "초록", "Green"),
                    QuizQuestion(R.drawable.colors_blue, "파랑", "Blue"),
                    QuizQuestion(R.drawable.colors_navy, "남색", "Navy"),
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.colors_purple, "보라", "Purple"),
                    QuizQuestion(R.drawable.colors_white, "흰색", "White"),
                    QuizQuestion(R.drawable.colors_black, "검정", "Black"),
                    QuizQuestion(R.drawable.colors_gray, "회색", "Gray"),
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.colors_red, "빨강", "Red"),
                    QuizQuestion(R.drawable.colors_gray, "회색", "Gray"),
                    QuizQuestion(R.drawable.colors_purple, "보라", "Purple"),
                    QuizQuestion(R.drawable.colors_black, "검정", "Black"),
                    QuizQuestion(R.drawable.colors_blue, "파랑", "Blue"),
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.colors_green, "초록", "Green"),
                    QuizQuestion(R.drawable.colors_yellow, "노랑", "Yellow"),
                    QuizQuestion(R.drawable.colors_navy, "남색", "Navy"),
                    QuizQuestion(R.drawable.colors_white, "흰색", "White"),
                    QuizQuestion(R.drawable.colors_orange, "주황", "Orange"),
                )
            ),
        )
    )

    object Animals : QuizCategory(
        "동물",
        R.drawable.animal_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.animal_dog, "강아지", "Dog"),
                    QuizQuestion(R.drawable.animal_cat, "고양이", "Cat"),
                    QuizQuestion(R.drawable.animal_tiger, "호랑이", "Tiger"),
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.animal_lion, "사자", "Lion"),
                    QuizQuestion(R.drawable.animal_horse, "말", "Horse"),
                    QuizQuestion(R.drawable.animal_sheep, "양", "Sheep"),
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.animal_pig, "돼지", "Pig"),
                    QuizQuestion(R.drawable.animal_chicken, "닭", "Chicken"),
                    QuizQuestion(R.drawable.animal_cow, "소", "cow"),
                    QuizQuestion(R.drawable.animal_rabbit, "토끼", "rabbit"),
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.animal_cat, "고양이", "Cat"),
                    QuizQuestion(R.drawable.animal_sheep, "양", "Sheep"),
                    QuizQuestion(R.drawable.animal_dog, "강아지", "Dog"),
                    QuizQuestion(R.drawable.animal_cow, "소", "cow"),
                    QuizQuestion(R.drawable.animal_lion, "사자", "Lion"),
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.animal_tiger, "호랑이", "Tiger"),
                    QuizQuestion(R.drawable.animal_chicken, "닭", "Chicken"),
                    QuizQuestion(R.drawable.animal_rabbit, "토끼", "rabbit"),
                    QuizQuestion(R.drawable.animal_pig, "돼지", "Pig"),
                    QuizQuestion(R.drawable.animal_horse, "말", "Horse"),
                )
            ),
        )
    )

    object Fruits : QuizCategory(
        "과일과 야채",
        R.drawable.fruitsvegies_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.fruitvegies_apple, "사과", "Apple"),
                    QuizQuestion(R.drawable.fruitvegies_banana, "바나나", "Banana"),
                    QuizQuestion(R.drawable.fruitvegies_watermelon, "수박", "Watermelon"),
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.fruitvegies_pear, "배", "Pear"),
                    QuizQuestion(R.drawable.fruitvegies_strawberry, "딸기", "Strawberry"),
                    QuizQuestion(R.drawable.fruitvegies_peach, "복숭아", "Peach"),
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.fruitvegies_orange, "오렌지", "Orange"),
                    QuizQuestion(R.drawable.fruitvegies_grape, "포도", "Grape"),
                    QuizQuestion(R.drawable.fruitvegies_mango, "망고", "Mango"),
                    QuizQuestion(R.drawable.fruitvegies_carrot, "당근", "Carrot"),
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.fruitvegies_apple, "사과", "Apple"),
                    QuizQuestion(R.drawable.fruitvegies_carrot, "당근", "Carrot"),
                    QuizQuestion(R.drawable.fruitvegies_pear, "배", "Pear"),
                    QuizQuestion(R.drawable.fruitvegies_strawberry, "딸기", "Strawberry"),
                    QuizQuestion(R.drawable.fruitvegies_orange, "오렌지", "Orange"),
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.fruitvegies_watermelon, "수박", "Watermelon"),
                    QuizQuestion(R.drawable.fruitvegies_peach, "복숭아", "Peach"),
                    QuizQuestion(R.drawable.fruitvegies_banana, "바나나", "Banana"),
                    QuizQuestion(R.drawable.fruitvegies_grape, "포도", "Grape"),
                    QuizQuestion(R.drawable.fruitvegies_mango, "망고", "Mango"),
                )
            ),
        )
    )

    object Transportation : QuizCategory(
        "이동수단",
        R.drawable.transportation_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.transportation_bicycle, "자전거", "Bicycle"),
                    QuizQuestion(R.drawable.transportation_car, "자동차", "Car"),
                    QuizQuestion(R.drawable.transportation_motorcycle, "오토바이", "Motorcycle"),
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.transportation_ship, "배", "Ship"),
                    QuizQuestion(R.drawable.transportation_airplane, "비행기", "Airplane"),
                    QuizQuestion(R.drawable.transportation_train, "기차", "train"),
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.transportation_bus, "버스", "Bus"),
                    QuizQuestion(R.drawable.transportation_helicopter, "헬리콥터", "Helicopter"),
                    QuizQuestion(R.drawable.transportation_excavator, "포크레인", "Excavator"),
                    QuizQuestion(R.drawable.transportation_tank, "탱크", "Tank"),
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.transportation_bicycle, "자전거", "Bicycle"),
                    QuizQuestion(R.drawable.transportation_train, "기차", "train"),
                    QuizQuestion(R.drawable.transportation_airplane, "비행기", "Airplane"),
                    QuizQuestion(R.drawable.transportation_tank, "탱크", "Tank"),
                    QuizQuestion(R.drawable.transportation_helicopter, "헬리콥터", "Helicopter"),
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.transportation_car, "자동차", "Car"),
                    QuizQuestion(R.drawable.transportation_ship, "배", "Ship"),
                    QuizQuestion(R.drawable.transportation_motorcycle, "오토바이", "Motorcycle"),
                    QuizQuestion(R.drawable.transportation_bus, "버스", "Bus"),
                    QuizQuestion(R.drawable.transportation_excavator, "포크레인", "Excavator"),
                )
            ),
        )
    )
///////////////////////////////////////////////////////////////사진 수정해야 함
    object Stationery : QuizCategory(
        "학용품",
        R.drawable.stationery_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.stationery_pencil, "연필", "Pencil"),
                    QuizQuestion(R.drawable.stationery_eraser, "지우개", "Eraser"),
                    QuizQuestion(R.drawable.stationery_pen, "펜", "Pen")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.stationery_pen, "크레파스", "Crayon"),
                    QuizQuestion(R.drawable.stationery_pen, "책", "Book"),
                    QuizQuestion(R.drawable.stationery_pen, "가방", "Bag")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.stationery_pen, "칠판", "Blackboard"),
                    QuizQuestion(R.drawable.stationery_pen, "풀", "Glue"),
                    QuizQuestion(R.drawable.stationery_pen, "자", "Ruler"),
                    QuizQuestion(R.drawable.stationery_pen, "가위", "Scissors")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.stationery_pen, "연필", "Pencil"),
                    QuizQuestion(R.drawable.stationery_pen, "가방", "Bag"),
                    QuizQuestion(R.drawable.stationery_pen, "칠판", "Blackboard"),
                    QuizQuestion(R.drawable.stationery_pen, "자", "Ruler"),
                    QuizQuestion(R.drawable.stationery_pen, "지우개", "Eraser")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.stationery_pen, "펜", "Pen"),
                    QuizQuestion(R.drawable.stationery_pen, "크레파스", "Crayon"),
                    QuizQuestion(R.drawable.stationery_pen, "책", "Book"),
                    QuizQuestion(R.drawable.stationery_pen, "가위", "Scissors"),
                    QuizQuestion(R.drawable.stationery_pen, "풀", "Glue")
                )
            )
        )
    )


    object Clothes : QuizCategory(
        "옷",
        R.drawable.clothes_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.clothes_tshirts, "티셔츠", "T-shirt"),
                    QuizQuestion(R.drawable.clothes_skirt, "치마", "Skirt"),
                    QuizQuestion(R.drawable.clothes_hat, "모자", "Hat")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.clothes_tshirts, "드레스", "Dress"),
                    QuizQuestion(R.drawable.clothes_tshirts, "바지", "Pants"),
                    QuizQuestion(R.drawable.clothes_tshirts, "장갑", "Gloves")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.clothes_tshirts, "양말", "Socks"),
                    QuizQuestion(R.drawable.clothes_tshirts, "스웨터", "Sweater"),
                    QuizQuestion(R.drawable.clothes_tshirts, "자켓", "Jacket"),
                    QuizQuestion(R.drawable.clothes_tshirts, "신발", "Shoes")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.clothes_tshirts, "티셔츠", "T-shirt"),
                    QuizQuestion(R.drawable.clothes_tshirts, "신발", "Shoes"),
                    QuizQuestion(R.drawable.clothes_tshirts, "장갑", "Gloves"),
                    QuizQuestion(R.drawable.clothes_tshirts, "드레스", "Dress"),
                    QuizQuestion(R.drawable.clothes_tshirts, "스웨터", "Sweater")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.clothes_tshirts, "치마", "Skirt"),
                    QuizQuestion(R.drawable.clothes_tshirts, "바지", "Pants"),
                    QuizQuestion(R.drawable.clothes_tshirts, "모자", "Hat"),
                    QuizQuestion(R.drawable.clothes_tshirts, "자켓", "Jacket"),
                    QuizQuestion(R.drawable.clothes_tshirts, "양말", "Socks")
                )
            )
        )
    )


    object Numbers : QuizCategory(
        "숫자",
        R.drawable.number_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.number_00, "0", "Zero"),
                    QuizQuestion(R.drawable.number_01, "1", "One"),
                    QuizQuestion(R.drawable.number_02, "2", "Two")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.number_00, "3", "Three"),
                    QuizQuestion(R.drawable.number_00, "4", "Four"),
                    QuizQuestion(R.drawable.number_00, "5", "Five")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.number_00, "6", "Six"),
                    QuizQuestion(R.drawable.number_00, "7", "Seven"),
                    QuizQuestion(R.drawable.number_00, "8", "Eight"),
                    QuizQuestion(R.drawable.number_00, "9", "Nine")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.number_00, "10", "Ten"),
                    QuizQuestion(R.drawable.number_00, "6", "Six"),
                    QuizQuestion(R.drawable.number_00, "3", "Three"),
                    QuizQuestion(R.drawable.number_00, "2", "Two"),
                    QuizQuestion(R.drawable.number_00, "9", "Nine")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.number_00, "8", "Eight"),
                    QuizQuestion(R.drawable.number_00, "1", "One"),
                    QuizQuestion(R.drawable.number_00, "5", "Five"),
                    QuizQuestion(R.drawable.number_00, "7", "Seven"),
                    QuizQuestion(R.drawable.number_00, "4", "Four")
                )
            )
        )
    )


    object Body : QuizCategory(
        "신체부위",
        R.drawable.body_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.body_eye, "눈", "Eye"),
                    QuizQuestion(R.drawable.body_nose, "코", "Nose"),
                    QuizQuestion(R.drawable.body_mouth, "입", "Mouth")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.body_eye, "귀", "Ear"),
                    QuizQuestion(R.drawable.body_eye, "무릎", "Knee"),
                    QuizQuestion(R.drawable.body_eye, "발", "Foot")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.body_eye, "어깨", "Shoulder"),
                    QuizQuestion(R.drawable.body_eye, "팔", "Arm"),
                    QuizQuestion(R.drawable.body_eye, "혀", "Tongue")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.body_eye, "손", "Hand"),
                    QuizQuestion(R.drawable.body_eye, "입", "Mouth"),
                    QuizQuestion(R.drawable.body_eye, "팔", "Arm"),
                    QuizQuestion(R.drawable.body_eye, "귀", "Ear"),
                    QuizQuestion(R.drawable.body_eye, "어깨", "Shoulder")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.body_eye, "눈", "Eye"),
                    QuizQuestion(R.drawable.body_eye, "발", "Foot"),
                    QuizQuestion(R.drawable.body_eye, "무릎", "Knee"),
                    QuizQuestion(R.drawable.body_eye, "코", "Nose"),
                    QuizQuestion(R.drawable.body_eye, "혀", "Tongue")
                )
            )
        )
    )


    object Week : QuizCategory(
        "일주일",
        R.drawable.week_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.week_mon, "월요일", "Monday"),
                    QuizQuestion(R.drawable.week_tue, "화요일", "Tuesday"),
                    QuizQuestion(R.drawable.week_wed, "수요일", "Wednesday")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.week_mon, "목요일", "Thursday"),
                    QuizQuestion(R.drawable.week_mon, "금요일", "Friday"),
                    QuizQuestion(R.drawable.week_mon, "토요일", "Saturday")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.week_mon, "일요일", "Sunday"),
                    QuizQuestion(R.drawable.week_mon, "평일", "Weekday"),
                    QuizQuestion(R.drawable.week_mon, "주말", "Weekend")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.week_mon, "월요일", "Monday"),
                    QuizQuestion(R.drawable.week_mon, "토요일", "Saturday"),
                    QuizQuestion(R.drawable.week_mon, "금요일", "Friday"),
                    QuizQuestion(R.drawable.week_mon, "평일", "Weekday"),
                    QuizQuestion(R.drawable.week_mon, "주말", "Weekend")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.week_mon, "화요일", "Tuesday"),
                    QuizQuestion(R.drawable.week_mon, "수요일", "Wednesday"),
                    QuizQuestion(R.drawable.week_mon, "목요일", "Thursday"),
                    QuizQuestion(R.drawable.week_mon, "일요일", "Sunday")
                )
            )
        )
    )


    object Months : QuizCategory(
        "달",
        R.drawable.months_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.months_01, "1월", "January"),
                    QuizQuestion(R.drawable.months_02, "2월", "February"),
                    QuizQuestion(R.drawable.months_03, "3월", "March")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.months_01, "4월", "April"),
                    QuizQuestion(R.drawable.months_01, "5월", "May"),
                    QuizQuestion(R.drawable.months_01, "6월", "June")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.months_01, "7월", "July"),
                    QuizQuestion(R.drawable.months_01, "8월", "August"),
                    QuizQuestion(R.drawable.months_01, "9월", "September"),
                    QuizQuestion(R.drawable.months_01, "10월", "October")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.months_01, "11월", "November"),
                    QuizQuestion(R.drawable.months_01, "12월", "December"),
                    QuizQuestion(R.drawable.months_01, "2월", "February"),
                    QuizQuestion(R.drawable.months_01, "9월", "September"),
                    QuizQuestion(R.drawable.months_01, "5월", "May")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.months_01, "3월", "March"),
                    QuizQuestion(R.drawable.months_01, "8월", "August"),
                    QuizQuestion(R.drawable.months_01, "1월", "January"),
                    QuizQuestion(R.drawable.months_01, "10월", "October"),
                    QuizQuestion(R.drawable.months_01, "6월", "June")
                )
            )
        )
    )

    object Weather : QuizCategory(
        "계절과 날씨",
        R.drawable.weather_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.weather_spring, "봄", "Spring"),
                    QuizQuestion(R.drawable.weather_summer, "여름", "Summer"),
                    QuizQuestion(R.drawable.weather_autumn, "가을", "Fall")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.weather_spring, "겨울", "Winter"),
                    QuizQuestion(R.drawable.weather_spring, "화창한", "Sunny"),
                    QuizQuestion(R.drawable.weather_spring, "더운", "Hot")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.weather_spring, "비", "Rainy"),
                    QuizQuestion(R.drawable.weather_spring, "눈", "Snowy"),
                    QuizQuestion(R.drawable.weather_spring, "추운", "Cold")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.weather_spring, "여름", "Summer"),
                    QuizQuestion(R.drawable.weather_spring, "비", "Rainy"),
                    QuizQuestion(R.drawable.weather_spring, "봄", "Spring"),
                    QuizQuestion(R.drawable.weather_spring, "화창한", "Sunny"),
                    QuizQuestion(R.drawable.weather_spring, "겨울", "Winter")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.weather_spring, "가을", "Fall"),
                    QuizQuestion(R.drawable.weather_spring, "추운", "Cold"),
                    QuizQuestion(R.drawable.weather_spring, "더운", "Hot"),
                    QuizQuestion(R.drawable.weather_spring, "눈", "Snowy"),
                    QuizQuestion(R.drawable.weather_spring, "더운", "Hot")
                )
            )
        )
    )

    object Shapes : QuizCategory(
        "모양",
        R.drawable.shapes_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.shapes_circle, "원", "Circle"),
                    QuizQuestion(R.drawable.shapes_triangle, "삼각형", "Triangle"),
                    QuizQuestion(R.drawable.shapes_rectangle, "사각형", "Square")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.shapes_circle, "별", "Star"),
                    QuizQuestion(R.drawable.shapes_circle, "하트", "Heart"),
                    QuizQuestion(R.drawable.shapes_circle, "다이아몬드", "Diamond")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.shapes_circle, "클로버", "Clover"),
                    QuizQuestion(R.drawable.shapes_circle, "화살표", "Arrow")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.shapes_circle, "다이아몬드", "Diamond"),
                    QuizQuestion(R.drawable.shapes_circle, "원", "Circle"),
                    QuizQuestion(R.drawable.shapes_circle, "하트", "Heart"),
                    QuizQuestion(R.drawable.shapes_circle, "화살표", "Arrow"),
                    QuizQuestion(R.drawable.shapes_circle, "삼각형", "Triangle")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.shapes_circle, "사각형", "Square"),
                    QuizQuestion(R.drawable.shapes_circle, "별", "Star"),
                    QuizQuestion(R.drawable.shapes_circle, "클로버", "Clover"),
                    QuizQuestion(R.drawable.shapes_circle, "화살표", "Arrow"),
                    QuizQuestion(R.drawable.shapes_circle, "삼각형", "Triangle")
                )
            )
        )
    )

    object Jobs : QuizCategory(
        "직업",
        R.drawable.jobs_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.jobs_teacher, "선생님", "Teacher"),
                    QuizQuestion(R.drawable.jobs_police, "경찰관", "Police"),
                    QuizQuestion(R.drawable.jobs_firefighter, "소방관", "Firefighter")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.jobs_teacher, "의사", "Doctor"),
                    QuizQuestion(R.drawable.jobs_teacher, "마술사", "Magician"),
                    QuizQuestion(R.drawable.jobs_teacher, "군인", "Soldier")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.jobs_teacher, "간호사", "Nurse"),
                    QuizQuestion(R.drawable.jobs_teacher, "운동선수", "Athlete"),
                    QuizQuestion(R.drawable.jobs_teacher, "가수", "Singer"),
                    QuizQuestion(R.drawable.jobs_teacher, "우주비행사", "Astronaut")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.jobs_teacher, "경찰관", "Police"),
                    QuizQuestion(R.drawable.jobs_teacher, "운동선수", "Athlete"),
                    QuizQuestion(R.drawable.jobs_teacher, "의사", "Doctor"),
                    QuizQuestion(R.drawable.jobs_teacher, "마술사", "Magician"),
                    QuizQuestion(R.drawable.jobs_teacher, "선생님", "Teacher")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.jobs_teacher, "요리사", "Chef"),
                    QuizQuestion(R.drawable.jobs_teacher, "가수", "Singer"),
                    QuizQuestion(R.drawable.jobs_teacher, "군인", "Soldier"),
                    QuizQuestion(R.drawable.jobs_teacher, "간호사", "Nurse"),
                    QuizQuestion(R.drawable.jobs_teacher, "우주비행사", "Astronaut")
                )
            )
        )
    )

    object SolarSystem : QuizCategory(
        "태양계",
        R.drawable.planet_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.planet_sun, "태양", "Sun"),
                    QuizQuestion(R.drawable.planet_mercury, "수성", "Mercury"),
                    QuizQuestion(R.drawable.planet_venus, "금성", "Venus")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.planet_sun, "지구", "Earth"),
                    QuizQuestion(R.drawable.planet_sun, "화성", "Mars"),
                    QuizQuestion(R.drawable.planet_sun, "목성", "Jupiter")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.planet_sun, "토성", "Saturn"),
                    QuizQuestion(R.drawable.planet_sun, "천왕성", "Uranus"),
                    QuizQuestion(R.drawable.planet_sun, "해왕성", "Neptune")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.planet_sun, "수성", "Mercury"),
                    QuizQuestion(R.drawable.planet_sun, "지구", "Earth"),
                    QuizQuestion(R.drawable.planet_sun, "화성", "Mars"),
                    QuizQuestion(R.drawable.planet_sun, "해왕성", "Neptune"),
                    QuizQuestion(R.drawable.planet_sun, "태양", "Sun")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.planet_sun, "금성", "Venus"),
                    QuizQuestion(R.drawable.planet_sun, "목성", "Jupiter"),
                    QuizQuestion(R.drawable.planet_sun, "토성", "Saturn"),
                    QuizQuestion(R.drawable.planet_sun, "천왕성", "Uranus"),
                    QuizQuestion(R.drawable.planet_sun, "해왕성", "Neptune")
                )
            )
        )
    )

    object Country : QuizCategory(
        "나라",
        R.drawable.country_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.country_korea, "한국", "South Korea"),
                    QuizQuestion(R.drawable.country_japan, "일본", "Japan"),
                    QuizQuestion(R.drawable.country_china, "중국", "China")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.country_korea, "미국", "United States"),
                    QuizQuestion(R.drawable.country_korea, "스위스", "Switzerland"),
                    QuizQuestion(R.drawable.country_korea, "캐나다", "Canada")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.country_korea, "호주", "Australia"),
                    QuizQuestion(R.drawable.country_korea, "독일", "Germany"),
                    QuizQuestion(R.drawable.country_korea, "영국", "United Kingdom"),
                    QuizQuestion(R.drawable.country_korea, "프랑스", "France")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.country_korea, "이탈리아", "Italy"),
                    QuizQuestion(R.drawable.country_korea, "러시아", "Russia"),
                    QuizQuestion(R.drawable.country_korea, "사우디", "Saudi Arabia"),
                    QuizQuestion(R.drawable.country_korea, "일본", "Japan"),
                    QuizQuestion(R.drawable.country_korea, "호주", "Australia")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.country_korea, "미국", "United States"),
                    QuizQuestion(R.drawable.country_korea, "스위스", "Switzerland"),
                    QuizQuestion(R.drawable.country_korea, "한국", "South Korea"),
                    QuizQuestion(R.drawable.country_korea, "독일", "Germany"),
                    QuizQuestion(R.drawable.country_korea, "중국", "China")
                )
            )
        )
    )

    object Food : QuizCategory(
        "음식",
        R.drawable.foods_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.foods_rice, "밥", "Rice"),
                    QuizQuestion(R.drawable.foods_bread, "빵", "Bread"),
                    QuizQuestion(R.drawable.foods_burger, "버거", "Burger")
                )
            ),
            QuizDay(
                2, listOf(
                    QuizQuestion(R.drawable.foods_rice, "피자", "Pizza"),
                    QuizQuestion(R.drawable.foods_rice, "치킨", "Chicken"),
                    QuizQuestion(R.drawable.foods_rice, "우유", "Milk")
                )
            ),
            QuizDay(
                3, listOf(
                    QuizQuestion(R.drawable.foods_rice, "국수", "Noodles"),
                    QuizQuestion(R.drawable.foods_rice, "케이크", "Cake"),
                    QuizQuestion(R.drawable.foods_rice, "치즈", "Cheese"),
                    QuizQuestion(R.drawable.foods_rice, "과자", "Snack")
                )
            ),
            QuizDay(
                4, listOf(
                    QuizQuestion(R.drawable.foods_rice, "밥", "Rice"),
                    QuizQuestion(R.drawable.foods_rice, "피자", "Pizza"),
                    QuizQuestion(R.drawable.foods_rice, "치킨", "Chicken"),
                    QuizQuestion(R.drawable.foods_rice, "국수", "Noodles"),
                    QuizQuestion(R.drawable.foods_rice, "과자", "Snack")
                )
            ),
            QuizDay(
                5, listOf(
                    QuizQuestion(R.drawable.foods_rice, "빵", "Bread"),
                    QuizQuestion(R.drawable.foods_rice, "버거", "Burger"),
                    QuizQuestion(R.drawable.foods_rice, "우유", "Milk"),
                    QuizQuestion(R.drawable.foods_rice, "케이크", "Cake"),
                    QuizQuestion(R.drawable.foods_rice, "치즈", "Cheese")
                )
            )
        )
    )


    companion object {
        val allCategories = listOf(Fruits, Animals, Colors, Jobs, Transportation, Stationery, Clothes, Numbers, Body, Week, Months, Weather, Shapes, SolarSystem, Country, Food)
    }
}

// CategoryDayManager를 QuizCategory 외부로 이동
object CategoryDayManager {
    private val categoryDays = mutableMapOf<String, Int>()
    private var currentCategoryName: String? = null

    init {
        QuizCategory.allCategories.forEach { category ->
            categoryDays[category.name] = 1
        }
    }

    fun incrementDay(categoryName: String) {
        categoryDays[categoryName] = categoryDays[categoryName]?.plus(1) ?: 1
    }

    fun getDay(categoryName: String): Int {
        return categoryDays[categoryName] ?: 1
    }

    fun setCurrentCategoryName(categoryName: String) {
        currentCategoryName = categoryName
    }

    fun getCurrentCategoryName(): String? {
        return currentCategoryName
    }
}