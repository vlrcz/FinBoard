package com.vlad.finboard.core.data.db.models

enum class CategoryName {
    CAFE {
        override fun toString(): String {
            return "Кафе"
        }
    },
    TOURISM {
        override fun toString(): String {
            return "Туризм"
        }
    },
    FOOD {
        override fun toString(): String {
            return "Продукты"
        }
    },
    TECHNICS {
        override fun toString(): String {
            return "Техника"
        }
    },
    HEALTH {
        override fun toString(): String {
            return "Здоровье"
        }
    },
    CLOTHES {
        override fun toString(): String {
            return "Одежда"
        }
    },
    GAMES {
        override fun toString(): String {
            return "Игры"
        }
    },
    SPORT {
        override fun toString(): String {
            return "Спорт"
        }
    },
    RENTAL {
        override fun toString(): String {
            return "Аренда"
        }
    },
    SALARY {
        override fun toString(): String {
            return "Зарплата"
        }
    },
    DEPOSITS {
        override fun toString(): String {
            return "Вклады"
        }
    },
    GRANTS {
        override fun toString(): String {
            return "Степендия"
        }
    },
    PENSION {
        override fun toString(): String {
            return "Пенсия"
        }
    }
}