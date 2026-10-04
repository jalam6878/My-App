package com.example.data.model

data class DailyRoutineGuide(
    val period: RoutinePeriod,
    val timeRange: String,
    val titleEn: String,
    val titleBn: String,
    val descEn: String,
    val descBn: String,
    val tipsEn: List<String>,
    val tipsBn: List<String>
)

enum class RoutinePeriod {
    MORNING, AFTERNOON, EVENING, NIGHT
}

data class HealthTipItem(
    val id: String,
    val category: String, // "Today", "Morning", "Food", "Exercise", "Nutrition", "Walking", "Sleep", "Mind"
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val actionStepEn: String,
    val actionStepBn: String,
    val readTimeMin: Int = 2
)

data class FoodNutritionItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val category: String, // "Grains", "Lentils", "Proteins", "Dairy", "Fruits", "Vegetables", "Nuts"
    val servingSizeEn: String,
    val servingSizeBn: String,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val fiberGrams: Float,
    val healthBenefitsEn: String,
    val healthBenefitsBn: String
)

data class ExerciseGuideItem(
    val id: String,
    val category: String, // "Beginner", "Walking", "Stretching", "Recovery"
    val titleEn: String,
    val titleBn: String,
    val durationMinutes: Int,
    val difficultyEn: String, // "Beginner", "Medium", "Advanced"
    val difficultyBn: String,
    val caloriesBurned: Int,
    val descriptionEn: String,
    val descriptionBn: String,
    val instructionsEn: List<String>,
    val instructionsBn: List<String>,
    val safetyTipsEn: String,
    val safetyTipsBn: String
)

data class MotivationQuoteItem(
    val id: String,
    val quoteEn: String,
    val quoteBn: String,
    val author: String,
    var isFavorite: Boolean = false
)

data class MealPlanItem(
    val mealType: String, // "Breakfast", "Lunch", "Dinner"
    val titleEn: String,
    val titleBn: String,
    val calories: Int,
    val itemsEn: List<String>,
    val itemsBn: List<String>,
    val notesEn: String,
    val notesBn: String
)
