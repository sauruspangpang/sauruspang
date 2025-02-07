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


sealed class QuizCategory(val name: String, val thumbnail :Int, val days: List<QuizDay>) {
    object Fruits : QuizCategory("과일과 야채",
        R.drawable.fruitsvegies_category,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.fruitvegies_apple, "사과", "Apple"),
            QuizQuestion(R.drawable.fruitvegies_banana, "바나나", "Banana"),
            QuizQuestion(R.drawable.fruitvegies_watermelon, "수박", "Watermelon")

        )),
        QuizDay(2, listOf(
            QuizQuestion(R.drawable.fruitvegies_pear, "배", "Pear"),
            QuizQuestion(R.drawable.fruitvegies_strawberry, "딸기", "Strawberry"),
            QuizQuestion(R.drawable.fruitvegies_peach, "복숭아", "Peach"),
        ))
    ))

    object Animals : QuizCategory("동물",
        R.drawable.animal_category,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.animal_dog, "강아지", "Dog"),
            QuizQuestion(R.drawable.animal_cat, "고양이", "Cat"),
            QuizQuestion(R.drawable.animal_tiger, "호랑이", "Tiger"),
        )),
        QuizDay(2, listOf(
            QuizQuestion(R.drawable.animal_lion, "사자", "Lion"),
            QuizQuestion(R.drawable.animal_horse, "말", "Horse"),
            QuizQuestion(R.drawable.animal_sheep, "양", "Sheep")
        ))
    ))

    object Colors : QuizCategory("색",
        R.drawable.colors_category,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.colors_red, "빨강", "Red"),
            QuizQuestion(R.drawable.colors_orange, "주황", "Orange"),
            QuizQuestion(R.drawable.colors_yellow, "노랑", "Yellow")
        ))
    ))

    object Jobs : QuizCategory("직업",
        R.drawable.jobs_category,
        listOf(
            QuizDay(1, listOf(
                QuizQuestion(R.drawable.jobs_teacher, "선생님", "Teacher"),
                QuizQuestion(R.drawable.jobs_police, "경찰관", "Police Officer"),
                QuizQuestion(R.drawable.jobs_firefighter, "소방관", "Firefighter")
            ))
        ))

    companion object {
        val allCategories = listOf(Fruits, Animals, Colors, Jobs)
    }
}