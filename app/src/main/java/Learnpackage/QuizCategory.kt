package Learnpackage

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
    object Fruits : QuizCategory("과일",
        R.drawable.fruit,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.apple, "사과", "Apple"),
            QuizQuestion(R.drawable.banana, "바나나", "Banana"),
            QuizQuestion(R.drawable.apple, "오렌지", "Orange")
        )),
        QuizDay(2, listOf(
            QuizQuestion(R.drawable.grape, "포도", "Grape"),
            QuizQuestion(R.drawable.peach, "복숭아", "Peach"),
            QuizQuestion(R.drawable.grape, "멜론", "Melon")
        ))
    ))

    object Animals : QuizCategory("동물",
        R.drawable.animals,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.cat, "고양이", "Cat"),
            QuizQuestion(R.drawable.cat, "강아지", "Dog"),
            QuizQuestion(R.drawable.cat, "사자", "Lion")
        )),
        QuizDay(2, listOf(
            QuizQuestion(R.drawable.elephant, "코끼리", "Elephant"),
            QuizQuestion(R.drawable.elephant, "호랑이", "Tiger"),
            QuizQuestion(R.drawable.elephant, "토끼", "Rabbit")
        ))
    ))

    object Colors : QuizCategory("색",
        R.drawable.color,
        listOf(
        QuizDay(1, listOf(
            QuizQuestion(R.drawable.red, "빨강", "Red"),
            QuizQuestion(R.drawable.red, "파랑", "Blue"),
            QuizQuestion(R.drawable.red, "초록", "Green")
        ))
    ))

    object Jobs : QuizCategory("직업",
        R.drawable.jobs,
        listOf(
            QuizDay(1, listOf(
                QuizQuestion(R.drawable.red, "개발자", "Developer"),
                QuizQuestion(R.drawable.red, "PM", "Product Manager"),
                QuizQuestion(R.drawable.red, "디자이너", "Designer")
            ))
        ))

    companion object {
        val allCategories = listOf(Fruits, Animals, Colors,Jobs)
    }
}