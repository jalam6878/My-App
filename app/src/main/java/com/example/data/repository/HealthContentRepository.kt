package com.example.data.repository

import com.example.data.local.AchievementEntity
import com.example.data.model.DailyRoutineGuide
import com.example.data.model.ExerciseGuideItem
import com.example.data.model.FoodNutritionItem
import com.example.data.model.HealthTipItem
import com.example.data.model.MealPlanItem
import com.example.data.model.MotivationQuoteItem
import com.example.data.model.RoutinePeriod
import java.util.Calendar

object HealthContentRepository {

    fun getCurrentRoutinePeriod(): RoutinePeriod {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 6..10 -> RoutinePeriod.MORNING
            in 11..14 -> RoutinePeriod.AFTERNOON
            in 15..18 -> RoutinePeriod.EVENING
            else -> RoutinePeriod.NIGHT
        }
    }

    val dailyRoutines: List<DailyRoutineGuide> = listOf(
        DailyRoutineGuide(
            period = RoutinePeriod.MORNING,
            timeRange = "6:00 AM – 11:00 AM",
            titleEn = "Morning Vitality & Activation",
            titleBn = "সকালের সতেজতা ও সূচনা",
            descEn = "Awaken your metabolism, rehydrate your cells, and kickstart your daily steps.",
            descBn = "মেটাবলিজম জাগ্রত করুন, শরীর হাইড্রেট করুন এবং হাঁটা শুরু করুন।",
            tipsEn = listOf(
                "Drink 1-2 glasses of warm water right upon waking.",
                "Take a 15-20 minute brisk morning walk in natural daylight.",
                "Have a balanced breakfast rich in protein and fiber."
            ),
            tipsBn = listOf(
                "ঘুম থেকে উঠে ১-২ গ্লাস হালকা গরম পানি পান করুন।",
                "ভোরের সূর্যের আলোয় ১৫-২০ মিনিট প্রাতঃভ্রমণ করুন।",
                "প্রোটিন ও ফাইবার সমৃদ্ধ পুষ্টিকর সকালের নাস্তা গ্রহণ করুন।"
            )
        ),
        DailyRoutineGuide(
            period = RoutinePeriod.AFTERNOON,
            timeRange = "11:00 AM – 3:00 PM",
            titleEn = "Midday Energy & Sustenance",
            titleBn = "দুপুরের কর্মশক্তি ও পুষ্টি",
            descEn = "Maintain focus, posture, and balanced nourishment through the work hours.",
            descBn = "কাজের ফাঁকে শারীরিক সচলতা ও সঠিক পুষ্টি বজায় রাখুন।",
            tipsEn = listOf(
                "Take a 2-minute posture stretch every 45 minutes of sitting.",
                "Consume a balanced plate: 50% vegetables/salad, 25% protein, 25% grains.",
                "Drink 500ml water 30 minutes before lunch."
            ),
            tipsBn = listOf(
                "প্রতি ৪৫ মিনিট বসার পর ২ মিনিটের জন্য শরীর টানটান করুন।",
                "প্লেটের অর্ধেক শাকসবজি, এক-চতুর্থাংশ প্রোটিন ও এক-চতুর্থাংশ শর্করা রাখুন।",
                "দুপুরের খাবারের ৩০ মিনিট পূর্বে ৫০০ মিলি পানি পান করুন।"
            )
        ),
        DailyRoutineGuide(
            period = RoutinePeriod.EVENING,
            timeRange = "3:00 PM – 7:00 PM",
            titleEn = "Evening Motion & Decompression",
            titleBn = "সন্ধ্যার হাঁটা ও ক্লান্তি দূরীকরণ",
            descEn = "Close your step gap, release work stress, and revitalize your cardiovascular system.",
            descBn = "দিনের অবশিষ্ট পদক্ষেপ পূরণ করুন এবং মানসিক ক্লান্তি দূর করুন।",
            tipsEn = listOf(
                "Aim for a dedicated 3,000-step continuous stroll or brisk walk.",
                "Choose healthy snacks: roasted chickpeas, nuts, or fresh seasonal fruit.",
                "Hydrate after evening physical activity."
            ),
            tipsBn = listOf(
                "সন্ধ্যায় বিরতিহীনভাবে ৩,০০০ পদক্ষেপ হাঁটার লক্ষ্য পূরণ করুন।",
                "স্বাস্থ্যকর নাস্তা হিসেবে ছোলা ভাজা, বাদাম বা মৌসুমি ফল খান।",
                "সন্ধ্যার হাঁটার পর পর্যাপ্ত পানি পান করুন।"
            )
        ),
        DailyRoutineGuide(
            period = RoutinePeriod.NIGHT,
            timeRange = "7:00 PM – 11:00 PM",
            titleEn = "Night Recovery & Restful Sleep",
            titleBn = "রাতের বিশ্রাম ও পুনরুজ্জীবন",
            descEn = "Prepare your muscles and mind for cell repair and deep restorative sleep.",
            descBn = "শরীর ও মনের ক্লান্তি দূর করে গভীর ও শান্ত ঘুমের প্রস্তুতি নিন।",
            tipsEn = listOf(
                "Finish dinner at least 2 hours before going to bed.",
                "A gentle 10-minute slow stroll aids digestion.",
                "Power down blue screens 30 minutes before sleep for melatonin secretion."
            ),
            tipsBn = listOf(
                "ঘুমাতে যাওয়ার অন্তত ২ ঘণ্টা পূর্বে রাতের খাবার সম্পন্ন করুন।",
                "খাওয়ার পর ১০ মিনিট ধীরেসুস্থে হাঁটলে হজমে দারুণ সহায়তা হয়।",
                "ঘুমের আধা ঘণ্টা আগে মোবাইল বা টিভি স্ক্রিন থেকে দূরে থাকুন।"
            )
        )
    )

    val healthTips: List<HealthTipItem> = listOf(
        HealthTipItem(
            id = "tip_1",
            category = "Today",
            titleEn = "Start Your Day With Warm Water",
            titleBn = "হালকা গরম পানি দিয়ে দিন শুরু করুন",
            descriptionEn = "Drinking a glass of lukewarm water first thing in the morning flushes toxins, rehydrates after 7-8 hours of sleep, and stimulates healthy peristalsis.",
            descriptionBn = "সকালে ঘুম থেকে উঠে এক গ্লাস হালকা গরম পানি পান করলে শরীরের বিপাক ক্রিয়া দ্রুত সক্রিয় হয় এবং পরিপাকতন্ত্র পরিষ্কার থাকে।",
            actionStepEn = "Keep a thermos flask near your bed or kitchen counter every night.",
            actionStepBn = "প্রতি রাতে শোবার পূর্বে এক ফ্লাস্ক হালকা গরম পানি হাতের কাছে রাখুন।"
        ),
        HealthTipItem(
            id = "tip_2",
            category = "Walking",
            titleEn = "Post-Meal 15-Minute Stroll",
            titleBn = "খাবারের পর ১৫ মিনিটের হাঁটা",
            descriptionEn = "A gentle 15-minute walk following lunch or dinner lowers postprandial glucose spikes by up to 22% and improves insulin sensitivity naturally.",
            descriptionBn = "দুপুরের বা রাতের খাবারের পর ১৫ মিনিট ধীরলয়ে হাঁটলে রক্তের শর্করার মাত্রা উল্লেখযোগ্যভাবে নিয়ন্ত্রণে থাকে।",
            actionStepEn = "Pace indoors or stroll in your terrace/veranda right after dining.",
            actionStepBn = "খাওয়ার পর শুয়ে না পড়ে বারান্দা বা ছাদে ধীরপায়ে কয়েক চক্কর দিন।"
        ),
        HealthTipItem(
            id = "tip_3",
            category = "Nutrition",
            titleEn = "Prioritize Dal & Lentil Fiber",
            titleBn = "ডাল ও আঁশযুক্ত খাবারের শক্তি",
            descriptionEn = "Traditional Indian lentils like Moong and Masoor provide plant protein, folate, and soluble fiber which feeds healthy gut flora and reduces LDL cholesterol.",
            descriptionBn = "মুগ ও মসুর ডাল উদ্ভিজ্জ প্রোটিন ও ফাইবার সমৃদ্ধ, যা কোলেস্টেরল কমায় ও পেটের স্বাস্থ্য ভালো রাখে।",
            actionStepEn = "Add one medium katori of well-cooked dal with a squeeze of fresh lemon.",
            actionStepBn = "প্রতিবেলার আহারে অন্তত এক বাটি ডাল ও সামান্য লেবুর রস যুক্ত করুন।"
        ),
        HealthTipItem(
            id = "tip_4",
            category = "Exercise",
            titleEn = "Consistent Brisk Cadence",
            titleBn = "নির্দিষ্ট গতিতে দ্রুত হাঁটা",
            descriptionEn = "Brisk walking at 100-115 steps per minute elevates your heart rate into Zone 2, where fat oxidation and mitochondrial efficiency peak safely.",
            descriptionBn = "প্রতি মিনিটে ১০০-১১০ কদম গতিতে হাঁটলে হৃৎপিণ্ড শক্তিশালী হয় এবং কার্যকরভাবে চর্বি ক্ষয় হয়।",
            actionStepEn = "Match your walking tempo to an upbeat rhythm during your evening session.",
            actionStepBn = "সন্ধ্যার হাঁটার সময় একটি সুন্দর ছন্দে সমান তালে কদম ফেলুন।"
        ),
        HealthTipItem(
            id = "tip_5",
            category = "Sleep",
            titleEn = "Sleep Temperature & Darkness",
            titleBn = "ঘুমের উপযুক্ত পরিবেশ",
            descriptionEn = "A dark, cool bedroom triggers the pineal gland to produce melatonin, facilitating deep REM stages for cellular repair and mental clarity.",
            descriptionBn = "অন্ধকার ও আরামদায়ক ঠান্ডা ঘরে মেলাটোনিন হরমোন ভালো নিঃসৃত হয় এবং গভীর ঘুম নিশ্চিত হয়।",
            actionStepEn = "Dim room lighting 1 hour prior to sleep and avoid bright overhead lights.",
            actionStepBn = "ঘুমানোর ১ ঘণ্টা পূর্বে ঘরের অতিরিক্ত উজ্জ্বল আলো নিভিয়ে দিন।"
        ),
        HealthTipItem(
            id = "tip_6",
            category = "Mind",
            titleEn = "Mindful Breath Counting",
            titleBn = "মনোযোগ দিয়ে গভীর শ্বাস নেওয়া",
            descriptionEn = "Five cycles of 4-second inhale and 6-second exhale stimulate the vagus nerve, reducing circulating cortisol and resting heart rate.",
            descriptionBn = "৪ সেকেন্ড ধরে শ্বাস নিয়ে ৬ সেকেন্ডে ধীরে ধীরে শ্বাস ছাড়লে মানসিক চাপ দ্রুত কমে যায়।",
            actionStepEn = "Practice 5 deep mindful breaths whenever you feel stressed during the day.",
            actionStepBn = "ক্লান্তি বা উদ্বেগের মুহূর্তে চোখ বন্ধ করে ৫ বার বুক ভরে শ্বাস নিন।"
        )
    )

    val indianFoods: List<FoodNutritionItem> = listOf(
        FoodNutritionItem(
            id = "food_rice",
            nameEn = "Steamed Brown/White Rice",
            nameBn = "ভাত (সিদ্ধ চাল)",
            category = "Grains",
            servingSizeEn = "1 Medium Katori (150g cooked)",
            servingSizeBn = "১ মাঝারি বাটি (১৫০ গ্রাম)",
            calories = 195,
            proteinGrams = 4.2f,
            carbsGrams = 43.0f,
            fatGrams = 0.6f,
            fiberGrams = 1.8f,
            healthBenefitsEn = "Easily digestible energy, low fat, hypoallergenic staple carbohydrate.",
            healthBenefitsBn = "সহজে হজমযোগ্য শর্করা, চর্বিমুক্ত এবং শরীরের প্রধান শক্তির উৎস।"
        ),
        FoodNutritionItem(
            id = "food_roti",
            nameEn = "Whole Wheat Roti / Chapati",
            nameBn = "আটার রুটি / চপাতি",
            category = "Grains",
            servingSizeEn = "1 Standard Roti (35g)",
            servingSizeBn = "১টি মাঝারি রুটি (৩৫ গ্রাম)",
            calories = 104,
            proteinGrams = 3.6f,
            carbsGrams = 21.0f,
            fatGrams = 0.5f,
            fiberGrams = 3.2f,
            healthBenefitsEn = "Rich in complex carbohydrates, insoluble dietary fiber for prolonged satiety.",
            healthBenefitsBn = "জটিল শর্করা ও আঁশে ভরপুর, দীর্ঘক্ষণ পেট ভরা রাখে ও রক্তে সুগার নিয়ন্ত্রণ করে।"
        ),
        FoodNutritionItem(
            id = "food_dal",
            nameEn = "Yellow Moong Dal (Tadka)",
            nameBn = "মুগ ডাল (তড়কা)",
            category = "Lentils",
            servingSizeEn = "1 Bowl (180g cooked)",
            servingSizeBn = "১ বাটি (১৮০ গ্রাম)",
            calories = 145,
            proteinGrams = 9.2f,
            carbsGrams = 22.0f,
            fatGrams = 2.4f,
            fiberGrams = 5.1f,
            healthBenefitsEn = "Very gentle on the stomach, high potassium, excellent plant protein.",
            healthBenefitsBn = "পেটের জন্য মৃদু, পটাসিয়াম ও প্রোটিনে সমৃদ্ধ, রোগ প্রতিরোধ ক্ষমতা বাড়ায়।"
        ),
        FoodNutritionItem(
            id = "food_fish",
            nameEn = "Rahu / Katla Fish Curry (Light)",
            nameBn = "রুই / কাতলা মাছের পাতলা ঝোল",
            category = "Proteins",
            servingSizeEn = "1 Medium Piece (100g)",
            servingSizeBn = "১ টুকরো (১০০ গ্রাম)",
            calories = 135,
            proteinGrams = 18.5f,
            carbsGrams = 1.2f,
            fatGrams = 5.8f,
            fiberGrams = 0.0f,
            healthBenefitsEn = "Premium lean protein with natural Omega-3 fatty acids for heart and brain.",
            healthBenefitsBn = "উন্নতমানের প্রোটিন ও ওমেগা-৩ ফ্যাটি এসিড যা হৃদপিণ্ড ও চোখের জন্য উপকারী।"
        ),
        FoodNutritionItem(
            id = "food_egg",
            nameEn = "Boiled Egg",
            nameBn = "সিদ্ধ ডিম",
            category = "Proteins",
            servingSizeEn = "1 Whole Large Egg (50g)",
            servingSizeBn = "১টি বড় ডিম (৫০ গ্রাম)",
            calories = 78,
            proteinGrams = 6.3f,
            carbsGrams = 0.6f,
            fatGrams = 5.3f,
            fiberGrams = 0.0f,
            healthBenefitsEn = "Complete amino acid profile, choline for cognitive health, Vitamin B12.",
            healthBenefitsBn = "সম্পূর্ণ অ্যামিনো এসিড, কোলিন ও ভিটামিন বি১২ সমৃদ্ধ আদর্শ খাদ্য।"
        ),
        FoodNutritionItem(
            id = "food_milk",
            nameEn = "Cow's Milk (Toned)",
            nameBn = "গরুর দুধ (টোনড)",
            category = "Dairy",
            servingSizeEn = "1 Glass (200ml)",
            servingSizeBn = "১ গ্লাস (২০০ মিলি)",
            calories = 122,
            proteinGrams = 6.4f,
            carbsGrams = 9.8f,
            fatGrams = 6.0f,
            fiberGrams = 0.0f,
            healthBenefitsEn = "High bioavailable calcium, phosphorus, and vitamin D for bone mineral density.",
            healthBenefitsBn = "হাড় ও দাঁত মজবুত করতে ক্যালসিয়াম এবং ভিটামিন ডি এর নির্ভরযোগ্য উৎস।"
        ),
        FoodNutritionItem(
            id = "food_curd",
            nameEn = "Fresh Curd / Dahi",
            nameBn = "টক দই / মিষ্টিহীন দই",
            category = "Dairy",
            servingSizeEn = "1 Small Cup (120g)",
            servingSizeBn = "১ কাপ (১২০ গ্রাম)",
            calories = 85,
            proteinGrams = 4.8f,
            carbsGrams = 5.5f,
            fatGrams = 4.2f,
            fiberGrams = 0.0f,
            healthBenefitsEn = "Live probiotic cultures that nourish gut microbiome and enhance immunity.",
            healthBenefitsBn = "প্রাকৃতিক প্রোবায়োটিক যা পেটের হজমশক্তি ও রোগ প্রতিরোধ ক্ষমতা বৃদ্ধি করে।"
        ),
        FoodNutritionItem(
            id = "food_oats",
            nameEn = "Rolled Oats (With Water/Milk)",
            nameBn = "ওটস খিচুড়ি বা ওটস দুধ",
            category = "Grains",
            servingSizeEn = "1 Bowl (40g dry cooked)",
            servingSizeBn = "১ বাটি (৪০ গ্রাম শুকনো)",
            calories = 155,
            proteinGrams = 5.4f,
            carbsGrams = 27.0f,
            fatGrams = 2.8f,
            fiberGrams = 4.0f,
            healthBenefitsEn = "Loaded with beta-glucan soluble fiber that binds and lowers blood cholesterol.",
            healthBenefitsBn = "বিটা-গ্লুকান ফাইবার সমৃদ্ধ যা কোলেস্টেরল কমায় ও হৃদযন্ত্র ভালো রাখে।"
        ),
        FoodNutritionItem(
            id = "food_banana",
            nameEn = "Banana (Ripe)",
            nameBn = "পাকা কলা (সবরি / সাগর)",
            category = "Fruits",
            servingSizeEn = "1 Medium Banana (118g)",
            servingSizeBn = "১টি মাঝারি কলা (১১৮ গ্রাম)",
            calories = 105,
            proteinGrams = 1.3f,
            carbsGrams = 27.0f,
            fatGrams = 0.3f,
            fiberGrams = 3.1f,
            healthBenefitsEn = "Rapid muscle replenishment, potassium prevents cramps during brisk walking.",
            healthBenefitsBn = "তাত্ক্ষণিক শক্তি যোগায় ও পটাসিয়াম সমৃদ্ধ হওয়ায় পেশির খিঁচুনি রোধ করে।"
        ),
        FoodNutritionItem(
            id = "food_apple",
            nameEn = "Fresh Apple",
            nameBn = "তাজা আপেল",
            category = "Fruits",
            servingSizeEn = "1 Medium Apple (150g)",
            servingSizeBn = "১টি মাঝারি আপেল (১৫০ গ্রাম)",
            calories = 80,
            proteinGrams = 0.5f,
            carbsGrams = 21.0f,
            fatGrams = 0.3f,
            fiberGrams = 4.4f,
            healthBenefitsEn = "Pectin fiber and quercetin antioxidants protect arteries and stabilize glucose.",
            healthBenefitsBn = "পেকটিন ফাইবার ও অ্যান্টিঅক্সিডেন্ট রক্তনালী সুস্থ রাখে ও শরীর সতেজ রাখে।"
        ),
        FoodNutritionItem(
            id = "food_guava",
            nameEn = "Guava / Peyara",
            nameBn = "দেশি পেয়ারা",
            category = "Fruits",
            servingSizeEn = "1 Medium Guava (120g)",
            servingSizeBn = "১টি পেয়ারা (১২০ গ্রাম)",
            calories = 68,
            proteinGrams = 2.6f,
            carbsGrams = 14.3f,
            fatGrams = 0.9f,
            fiberGrams = 5.4f,
            healthBenefitsEn = "Extraordinary Vitamin C (4x of oranges) and low glycemic index.",
            healthBenefitsBn = "কমলার চেয়ে ৪ গুণ বেশি ভিটামিন সি এবং ডায়াবেটিস নিয়ন্ত্রণে অত্যন্ত কার্যকর।"
        ),
        FoodNutritionItem(
            id = "food_pomegranate",
            nameEn = "Pomegranate / Dalim",
            nameBn = "বেদানা / ডালিম",
            category = "Fruits",
            servingSizeEn = "1/2 Cup Seeds (87g)",
            servingSizeBn = "আধা কাপ দানা (৮৭ গ্রাম)",
            calories = 72,
            proteinGrams = 1.5f,
            carbsGrams = 16.0f,
            fatGrams = 1.0f,
            fiberGrams = 3.5f,
            healthBenefitsEn = "Potent punicalagins antioxidants boost nitric oxide and improve vascular blood flow.",
            healthBenefitsBn = "শক্তিশালী অ্যান্টিঅক্সিডেন্ট যা রক্ত সঞ্চালন বৃদ্ধি করে ও রক্তচাপ স্বাভাবিক রাখে।"
        ),
        FoodNutritionItem(
            id = "food_almonds",
            nameEn = "Raw / Soaked Almonds",
            nameBn = "কাঠবাদাম (ভেজানো)",
            category = "Nuts",
            servingSizeEn = "6-8 Kernels (10g)",
            servingSizeBn = "৬-৮টি বাদাম (১০ গ্রাম)",
            calories = 58,
            proteinGrams = 2.1f,
            carbsGrams = 2.2f,
            fatGrams = 5.0f,
            fiberGrams = 1.2f,
            healthBenefitsEn = "Vitamin E, magnesium, and heart-healthy monounsaturated fats.",
            healthBenefitsBn = "ভিটামিন ই, ম্যাগনেসিয়াম এবং হৃদযন্ত্রের জন্য উপকারী স্বাস্থ্যকর চর্বি।"
        )
    )

    val exerciseGuides: List<ExerciseGuideItem> = listOf(
        ExerciseGuideItem(
            id = "ex_brisk_walk",
            category = "Walking",
            titleEn = "Brisk Walking (Power Cadence)",
            titleBn = "দ্রুত হাঁটা (পাওয়ার ক্যাডেন্স)",
            durationMinutes = 25,
            difficultyEn = "Medium",
            difficultyBn = "মাঝারি",
            caloriesBurned = 140,
            descriptionEn = "An intentional, rhythmic walking workout designed to elevate heart rate safely and burn calories while preserving joint health.",
            descriptionBn = "একটি ছন্দময় ও সচেতন হাঁটা যা জয়েন্টের ওপর চাপ না ফেলে হার্টরেট বাড়ায় ও ক্যালরি পোড়ায়।",
            instructionsEn = listOf(
                "Maintain upright posture with shoulders relaxed and chin parallel to the ground.",
                "Bend elbows at roughly 90 degrees and swing naturally from shoulders.",
                "Strike the ground lightly with your heel and roll through to push off toes.",
                "Breathe rhythmically in sync with your footfalls (3 steps inhale, 3 steps exhale)."
            ),
            instructionsBn = listOf(
                "মেরুদণ্ড সোজা রাখুন, কাঁধ শিথিল রাখুন এবং দৃষ্টি সামনের দিকে রাখুন।",
                "কনুই ৯০ ডিগ্রিতে বাঁকিয়ে হাতের স্বাভাবিক দুলুনি বজায় রাখুন।",
                "প্রথমে গোড়ালি মাটিতে ফেলুন এবং আলতোভাবে পায়ের আঙুল দিয়ে চাপ দিন।",
                "পায়ের পদক্ষেপের সাথে তাল মিলিয়ে ছন্দবদ্ধভাবে শ্বাস-প্রশ্বাস নিন।"
            ),
            safetyTipsEn = "Wear supportive cushioned athletic shoes; stay hydrated in warm humidity.",
            safetyTipsBn = "আরামদায়ক জুতো ব্যবহার করুন এবং গরমে পর্যাপ্ত পানি সাথে রাখুন।"
        ),
        ExerciseGuideItem(
            id = "ex_warmup",
            category = "Beginner",
            titleEn = "Dynamic Joint Warm-Up",
            titleBn = "হাঁটার পূর্বে ওয়ার্ম-আপ",
            durationMinutes = 5,
            difficultyEn = "Beginner",
            difficultyBn = "সহজ",
            caloriesBurned = 25,
            descriptionEn = "Prepares ankles, knees, hips, and lower back with synovial fluid before physical stride.",
            descriptionBn = "হাঁটা শুরুর পূর্বে পায়ের গোড়ালি, হাঁটু ও কোমরের জয়েন্ট সচল করার বিশেষ ব্যায়াম।",
            instructionsEn = listOf(
                "10 ankle rotations clockwise and counter-clockwise per foot.",
                "10 high knee marches in place at a gentle cadence.",
                "10 hip circles and gentle torso twists."
            ),
            instructionsBn = listOf(
                "উভয় পায়ের গোড়ালি ১০ বার ঘড়ির কাঁটার দিকে ও বিপরীত দিকে ঘুরান।",
                "এক জায়গায় দাঁড়িয়ে হালকাভাবে হাঁটু উঁচিয়ে ১০ বার মার্চ করুন।",
                "কোমর দুদিকে হালকা বৃত্তাকারে ঘুরিয়ে শরীর প্রস্তুত করুন।"
            ),
            safetyTipsEn = "Avoid jerky ballistic movements; warm smoothly.",
            safetyTipsBn = "কোনো ঝাঁকুনি না দিয়ে ধীরে ধীরে শরীরের প্রতিটি সন্ধি সচল করুন।"
        ),
        ExerciseGuideItem(
            id = "ex_stretching",
            category = "Stretching",
            titleEn = "Post-Walk Calf & Hamstring Stretch",
            titleBn = "হাঁটার পরের স্ট্রেচিং",
            durationMinutes = 8,
            difficultyEn = "Beginner",
            difficultyBn = "সহজ",
            caloriesBurned = 35,
            descriptionEn = "Elongates tight posterior chain muscles to prevent soreness and maintain flexibility.",
            descriptionBn = "হাঁটার পর পেশির টান ও ব্যথা দূর করতে এবং নমনীয়তা বজায় রাখতে গুরুত্বপূর্ণ।",
            instructionsEn = listOf(
                "Wall calf stretch: Press hands on wall, step one leg back, heel flat on floor for 20 seconds.",
                "Standing hamstring stretch: Extend one heel forward, hinge from hips with flat back.",
                "Quad stretch: Hold ankle behind glute while balancing against a support."
            ),
            instructionsBn = listOf(
                "দেয়ালে হাত রেখে এক পা পিছনে নিয়ে গোড়ালি মাটিতে চেপে ২০ সেকেন্ড রাখুন।",
                "এক পা সামান্য সামনে বাড়িয়ে কোমর থেকে হালকা সামনের দিকে ঝুঁকুন।",
                "হাতের সাহায্যে পায়ের গোড়ালি পিছনের দিকে টেনে ধরে উরুর পেশি টানটান করুন।"
            ),
            safetyTipsEn = "Hold stretches statically without bouncing; breathe steadily.",
            safetyTipsBn = "স্ট্রেচিং এর সময় অতিরিক্ত টান দেবেন না বা শরীর দোলাবেন না।"
        ),
        ExerciseGuideItem(
            id = "ex_cooldown",
            category = "Recovery",
            titleEn = "Gentle Cool-Down & Breath Recovery",
            titleBn = "কুল-ডাউন ও শিথিলকরণ",
            durationMinutes = 5,
            difficultyEn = "Beginner",
            difficultyBn = "সহজ",
            caloriesBurned = 15,
            descriptionEn = "Brings elevated pulse rate smoothly back to resting baseline, preventing blood pooling.",
            descriptionBn = "হৃদস্পন্দনের গতি ধীরে ধীরে স্বাভাবিক অবস্থায় ফিরিয়ে আনতে সাহায্য করে।",
            instructionsEn = listOf(
                "Slow strolling for 3 minutes tapering step speed.",
                "Deep diaphragmatic belly breathing with arms sweeping upward."
            ),
            instructionsBn = listOf(
                "৩ মিনিট অত্যন্ত ধীরগতিতে হেঁটে কদমের গতি কমিয়ে আনুন।",
                "হাত উপরে তুলে বুক ভরে গভীর শ্বাস নিন এবং ধীরে ধীরে ছেড়ে দিন।"
            ),
            safetyTipsEn = "Never sit down abruptly immediately after high-cadence walking.",
            safetyTipsBn = "দ্রুত হাঁটা শেষ করেই সাথে সাথে ধপ করে বসে পড়বেন না।"
        )
    )

    val mealPlans: List<MealPlanItem> = listOf(
        MealPlanItem(
            mealType = "Breakfast",
            titleEn = "Energizing Whole-Grain Breakfast",
            titleBn = "শক্তিদায়ক সকালের পুষ্টিকর নাস্তা",
            calories = 320,
            itemsEn = listOf(
                "Oats with fresh sliced fruits (banana/apple)",
                "1 Boiled whole egg or 1 small bowl sprouts",
                "5-6 Soaked raw almonds",
                "1 Glass warm water or light green tea"
            ),
            itemsBn = listOf(
                "ওটস সাথে ফল (কলা বা আপেল)",
                "১টি সিদ্ধ ডিম অথবা ১ বাটি অঙ্কুরিত ছোলা",
                "৫-৬টি ভেজানো কাঠবাদাম",
                "১ গ্লাস হালকা গরম পানি বা গ্রিন টি"
            ),
            notesEn = "Provides sustained glucose release without morning energy crashes.",
            notesBn = "সকালে দীর্ঘস্থায়ী শক্তি যোগায় এবং অলসতা দূর করে।"
        ),
        MealPlanItem(
            mealType = "Lunch",
            titleEn = "Balanced Rainbow Lunch Plate",
            titleBn = "সুষম দুপুরের রঙিন আহার",
            calories = 480,
            itemsEn = listOf(
                "1 Katori steamed rice or 2 whole-wheat rotis",
                "1 Bowl fresh vegetable salad (cucumber, tomato, carrot)",
                "1 Katori yellow dal or mixed lentil",
                "1 Piece fish curry or 50g paneer / tofu"
            ),
            itemsBn = listOf(
                "১ বাটি ভাত অথবা ২টি আটার পাতলা রুটি",
                "১ বাটি তাজা সালাদ (শসা, টমেটো, গাজর)",
                "১ বাটি মসুর বা মুগ ডাল",
                "১ টুকরো মাছের পাতলা ঝোল অথবা ৫০ গ্রাম পনির"
            ),
            notesEn = "High fiber content blunts glycemic spikes and keeps digestion optimal.",
            notesBn = "আঁশযুক্ত খাবার রক্তের শর্করার ভারসাম্য বজায় রাখতে অত্যন্ত সহায়ক।"
        ),
        MealPlanItem(
            mealType = "Dinner",
            titleEn = "Light & Restorative Dinner",
            titleBn = "হালকা ও সহজপাচ্য রাতের খাবার",
            calories = 360,
            itemsEn = listOf(
                "1-2 Thin multigrain rotis or clear vegetable soup",
                "1 Small bowl seasonal gourd / bottle gourd subzi (low oil)",
                "1 Small cup plain fresh curd or warm milk before bed"
            ),
            itemsBn = listOf(
                "১-২টি পাতলা রুটি অথবা সবজির স্যুপ",
                "১ ছোট বাটি লাউ বা পেঁপে তরকারি (স্বল্প তেলে)",
                "১ ছোট কাপ টক দই অথবা শোবার আগে এক কাপ দুধ"
            ),
            notesEn = "Light dinner promotes better quality sleep and prevents nocturnal reflux.",
            notesBn = "হালকা খাবার রাতে পরিপাকতন্ত্রকে বিশ্রাম দেয় এবং নির্বিঘ্ন ঘুম নিশ্চিত করে।"
        )
    )

    val motivationQuotes: List<MotivationQuoteItem> = listOf(
        MotivationQuoteItem(
            id = "quote_1",
            quoteEn = "A healthier you is a happier you. Every step you take today writes your tomorrow.",
            quoteBn = "একটু আজকের চেষ্টা, একটি সুন্দর আগামীকাল!! প্রতিটি পদক্ষেপ আপনাকে নতুন সম্ভাবনার দিকে নিয়ে যায়।",
            author = "VitalStep Wisdom"
        ),
        MotivationQuoteItem(
            id = "quote_2",
            quoteEn = "Small steps today, a healthier tomorrow. Consistency always defeats intensity.",
            quoteBn = "আজ একটু হাঁটেন, আগামীকাল আরো সুস্থ থাকুন! ধারাবাহিকতাই সাফল্যের চাবিকাঠি।",
            author = "Wellness Coach"
        ),
        MotivationQuoteItem(
            id = "quote_3",
            quoteEn = "Take care of your body. It is the only permanent place you have to live in.",
            quoteBn = "আপনার শরীরের যত্ন নিন। এটিই একমাত্র স্থায়ী ঠিকানা যেখানে আপনাকে সারাজীবন বাস করতে হবে।",
            author = "Jim Rohn"
        ),
        MotivationQuoteItem(
            id = "quote_4",
            quoteEn = "Walking is man's best medicine. A rhythm of steps clarifies both mind and body.",
            quoteBn = "হাঁটা হলো মানুষের শ্রেষ্ঠ প্রাকৃতিক ওষুধ। কদমের ছন্দ মন ও শরীর উভয়কেই সতেজ করে।",
            author = "Hippocrates"
        ),
        MotivationQuoteItem(
            id = "quote_5",
            quoteEn = "You don't have to be extreme, just consistent. 10,000 steps begin with a single stride.",
            quoteBn = "চরমপন্থার প্রয়োজন নেই, প্রয়োজন নিয়মিত চর্চার। ১০,০০০ পদক্ষেপ শুরু হয় মাত্র একটি কদমে।",
            author = "VitalStep Philosophy"
        )
    )

    val defaultAchievements: List<AchievementEntity> = listOf(
        AchievementEntity("step_1k", "First Steps", "প্রথম পদক্ষেপ", "Walk your first 1,000 steps", "প্রথম ১,০০০ পদক্ষেপ সম্পন্ন করুন", 1000, "steps", "footsteps", false),
        AchievementEntity("step_2k", "Little Mover", "ছোট্ট পথচলা", "Reach 2,000 steps in a day", "দিনে ২,০০০ পদক্ষেপে পৌঁছান", 2000, "steps", "speed", false),
        AchievementEntity("step_3k", "Active Soul", "সক্রিয় প্রাণ", "Reach 3,000 steps milestone", "৩,০০০ পদক্ষেপের মাইলফলক অর্জন করুন", 3000, "steps", "directions_walk", false),
        AchievementEntity("step_5k", "Daily Explorer", "দৈনিক অভিযাত্রী", "Complete 5,000 daily steps", "দৈনিক ৫,০০০ পদক্ষেপ হাঁটা সম্পন্ন করুন", 5000, "steps", "explore", false),
        AchievementEntity("step_6k", "Energy Booster", "শক্তির নতুন সূচনা", "Reach 6,000 steps with great stamina", "অসাধারণ শক্তিতে ৬,০০০ পদক্ষেপে পৌঁছান!", 6000, "steps", "bolt", false),
        AchievementEntity("step_8k", "Fitness Rising", "ফিটনেস জাগরণ", "Surpass 8,000 steps today", "আজ ৮,০০০ পদক্ষেপ অতিক্রম করুন", 8000, "steps", "trending_up", false),
        AchievementEntity("step_10k", "Step Champion", "স্টেপ চ্যাম্পিয়ন", "Achieve the gold standard 10,000 steps", "১০,০০০ পদক্ষেপের স্বর্ণালী লক্ষ্য স্পর্শ করুন!", 10000, "steps", "military_tech", false),
        AchievementEntity("step_12k", "Walking Hero", "হাঁটার বীর", "Conquer 12,000 steps with glory", "গৌরবের সাথে ১২,০০০ পদক্ষেপ জয় করুন", 12000, "steps", "workspace_premium", false),
        AchievementEntity("step_15k", "Endurance Master", "সহনশীলতা ওস্তাদ", "Push boundaries to 15,000 steps", "সীমানা ছাড়িয়ে ১৫,০০০ পদক্ষেপ অর্জন করুন", 15000, "steps", "sports_score", false),
        AchievementEntity("step_20k", "Elite Walker", "এলিট ওয়াকার", "Massive achievement of 20,000 steps", "২০,০০০ পদক্ষেপের অবিশ্বাস্য কৃতিত্ব!", 20000, "steps", "stars", false),
        AchievementEntity("step_25k", "Legendary Strider", "কিংবদন্তি পদাতিক", "Legendary milestone of 25,000 steps", "কিংবদন্তিসম ২৫,০০০ পদক্ষেপ!", 25000, "steps", "emoji_events", false),
        AchievementEntity("step_30k", "Ultimate Mover", "চরম অপরাজেয়", "God-tier endurance of 30,000 steps", "৩০,০০০ পদক্ষেপের অবিস্মরণীয় শিখর!", 30000, "steps", "diamond", false),

        // Streaks
        AchievementEntity("streak_1d", "First Day", "প্রথম দিন", "Complete your first recorded day", "অ্যাপে আপনার প্রথম সফল দিন সম্পন্ন করুন", 1, "streaks", "event_available", false),
        AchievementEntity("streak_3d", "3-Day Streak", "৩ দিনের ধারাবাহিকতা", "Maintain step goal for 3 consecutive days", "টানা ৩ দিন পদক্ষেপের লক্ষ্য বজায় রাখুন", 3, "streaks", "local_fire_department", false),
        AchievementEntity("streak_7d", "7-Day Streak", "৭ দিনের ধারাবাহিকতা", "Unstoppable week of 7 active days", "টানা ৭ দিন সক্রিয় থাকার অসাধারণ সাফল্য", 7, "streaks", "whatshot", false),
        AchievementEntity("streak_30d", "30-Day Master", "৩০ দিনের মাস্টার", "One month of unbroken health discipline", "এক মাসের অবিচল স্বাস্থ্য শৃঙ্খলা", 30, "streaks", "workspace_premium", false),

        // Special
        AchievementEntity("special_hydra", "Hydration Hero", "পানীয় চ্যাম্পিয়ন", "Reach daily hydration target 2.5L", "দৈনিক ২.৫ লিটার পানি পানের লক্ষ্য পূরণ করুন", 2500, "special", "water_drop", false),
        AchievementEntity("special_weight", "Weight Logged", "ওজন রেকর্ড", "Log your first weight entry", "প্রথমবারের মতো আপনার ওজন রেকর্ড করুন", 1, "special", "monitor_weight", false)
    )
}
