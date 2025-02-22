package com.ksj.sauruspang.Learnpackage

import android.util.Log
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

    object phonics : QuizCategory(
        "ABC",
        R.drawable.colors_category,
        listOf(
            QuizDay(
                1, listOf(
                    QuizQuestion(R.drawable.colors_red, "빨강", "Red"),
                    QuizQuestion(R.drawable.colors_orange, "주황", "Orange"),
                    QuizQuestion(R.drawable.colors_yellow, "노랑", "Yellow"),
                )
            )
        )
    )

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
                )
            )
        )

        object Clothes : QuizCategory(
            "옷",
            R.drawable.clothes_category,
            listOf(
                QuizDay(
                    1, listOf(
                        QuizQuestion(R.drawable.clothes_tshirts, "티셔츠", "T-shirts"),
                        QuizQuestion(R.drawable.clothes_skirt, "치마", "Skirt"),
                        QuizQuestion(R.drawable.clothes_hat, "모자", "Hat")
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
                        QuizQuestion(R.drawable.number_02, "2", "Two"),
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
                        QuizQuestion(R.drawable.body_mouth, "입", "Mouth"),
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
                        QuizQuestion(R.drawable.week_wed, "수요일", "Wednesday"),
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
                        QuizQuestion(R.drawable.months_03, "3월", "March"),
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
                        QuizQuestion(R.drawable.weather_autumn, "가을", "Fall"),
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
                        QuizQuestion(R.drawable.shapes_circle, "동그라미", "Circle"),
                        QuizQuestion(R.drawable.shapes_triangle, "세모", "Triangle"),
                        QuizQuestion(R.drawable.shapes_rectangle, "네모", "Square"),
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
                        QuizQuestion(R.drawable.jobs_teacher, "선생님", "Teacher"),
                        QuizQuestion(R.drawable.jobs_police, "경찰관", "Police"),
                        QuizQuestion(R.drawable.jobs_firefighter, "소방관", "Firefighter")
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
                        QuizQuestion(R.drawable.planet_venus, "금성", "Venus"),
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
                        QuizQuestion(R.drawable.country_china, "중국", "china"),
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
                        QuizQuestion(R.drawable.foods_burger, "버거", "Burger"),
                    )
                )
            )
        )

                companion object {
            val allCategories = listOf(
                phonics,
                Fruits,
                Animals,
                Colors,
                Jobs,
                Transportation,
                Stationery,
                Clothes,
                Numbers,
                Body,
                Week,
                Months,
                Weather,
                Shapes,
                SolarSystem,
                Country,
                Food
            )
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