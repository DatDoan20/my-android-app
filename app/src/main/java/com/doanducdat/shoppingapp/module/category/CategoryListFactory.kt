package com.doanducdat.shoppingapp.module.category

import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.utils.AppConstants

class CategoryListFactory {
    val c = AppConstants.Category
    val t = AppConstants.Type

    companion object {
        @Volatile
        private var instance: CategoryListFactory? = null

        fun getInstance(): CategoryListFactory {
            if (instance == null) {
                instance = CategoryListFactory()
            }
            return instance!!
        }
    }

    fun hotCategory(): MutableList<Category> {

        return mutableListOf(
            Category(R.drawable.nu_vay_suong, c.NU, t.VAY_SUONG, "Váy nữ"),
            Category(R.drawable.nu_chan_vay, c.NU, t.CHAN_VAY, "Chân váy"),
            Category(
                R.drawable.nu_ao_thun_tay_ngan,
                c.NU,
                t.AO_THUN_TAY_NGAN,
                "Áo thun tay ngắn nữ"
            ),
            Category(R.drawable.nu_do_the_thao, c.NU, t.DO_THE_THAO, "Đồ thể thao nữ"),
            Category(R.drawable.nu_quan_thun_dai, c.NU, t.QUAN_THUN_DAI, "Quần thun dài nữ"),

            Category(
                R.drawable.nam_ao_so_mi_tay_dai,
                c.NAM,
                t.AO_SO_MI_TAY_DAI,
                "Áo sơ mi tay dài nam"
            ),
            Category(
                R.drawable.nam_ao_thun_tay_ngan,
                c.NAM,
                t.AO_THUN_TAY_NGAN,
                "Áo thun tay ngắn nam"
            ),
            Category(R.drawable.nam_do_the_thao, c.NAM, t.DO_THE_THAO, "Đồ thể thao nam"),
            Category(R.drawable.nam_quan_thun_ngan, c.NAM, t.QUAN_THUN_NGAN, "Quần short thun nam"),
            Category(R.drawable.non, c.NamAndNu, t.NON, "Nón")
        )
    }

    fun womanDress(): MutableList<Category> {
        return mutableListOf(
            Category(R.drawable.nu_vay_suong, c.NU, t.VAY_SUONG, "Váy nữ"),
            Category(R.drawable.nu_chan_vay, c.NU, t.CHAN_VAY, "Chân váy"),
        )
    }

    fun womanShirt(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nu_ao_thun_tay_ngan,
                c.NU,
                t.AO_THUN_TAY_NGAN,
                "Áo thun tay ngắn nữ"
            ),
            Category(R.drawable.nu_ao_thun_tay_dai, c.NU, t.AO_THUN_TAY_DAI, "Áo thun tay dài nữ"),
            Category(
                R.drawable.nu_ao_so_mi_tay_ngan,
                c.NU,
                t.AO_SO_MI_TAY_NGAN,
                "Áo sơ mi nữ tay ngắn"
            ),
            Category(
                R.drawable.nu_ao_so_mi_tay_dai,
                c.NU,
                t.AO_SO_MI_TAY_DAI,
                "áo sơ mi nữ tay dài"
            ),
        )
    }

    fun womanTrouser(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nu_quan_thun_dai,
                c.NU,
                t.QUAN_THUN_DAI,
                "Quần thun dài nữ"
            ),
            Category(
                R.drawable.nu_quan_thun_ngan,
                c.NU,
                t.QUAN_THUN_NGAN,
                "Quần thun ngắn nữ"
            )
        )
    }

    fun womanHomeWear(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nu_do_bo,
                c.NU,
                t.DO_BO,
                "Đồ bộ nữ"
            ),
        )
    }

    fun womanCoat(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nu_ao_khoac,
                c.NU,
                t.AO_KHOAC,
                "Áo khoác nữ"
            ),
        )
    }

    fun womanSportWear(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nu_do_the_thao,
                c.NU,
                t.DO_THE_THAO,
                "Đồ thể thao nữ"
            ),
        )
    }

    fun manShirt(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nam_ao_thun_tay_ngan,
                c.NAM,
                t.AO_THUN_TAY_NGAN,
                "Áo thun tay ngắn"
            ),
            Category(
                R.drawable.nam_ao_thun_tay_dai,
                c.NAM,
                t.AO_THUN_TAY_DAI,
                "Áo thun tay dài"
            ),
            Category(
                R.drawable.nam_ao_so_mi_tay_ngan,
                c.NAM,
                t.AO_SO_MI_TAY_NGAN,
                "Áo sơ mi tay ngắn "
            ),
            Category(
                R.drawable.nam_ao_so_mi_tay_dai,
                c.NAM,
                t.AO_SO_MI_TAY_DAI,
                "Áo sơ mi tay dài"
            ),
        )
    }

    fun manTrouser(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nam_quan_thun_dai,
                c.NAM,
                t.QUAN_THUN_DAI,
                "Quần thun dài nam"
            ),
            Category(
                R.drawable.nam_quan_thun_ngan,
                c.NAM,
                t.QUAN_THUN_NGAN,
                "Quần thun ngắn nam"
            )
        )
    }

    fun manHomeWear(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nam_do_bo,
                c.NAM,
                t.DO_BO,
                "Đồ bộ nam"
            ),
        )
    }

    fun manCoat(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nam_ao_khoac,
                c.NAM,
                t.AO_KHOAC,
                "Áo khoác nam"
            ),
        )
    }

    fun manSportWear(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.drawable.nam_do_the_thao,
                c.NAM,
                t.DO_THE_THAO,
                "Đồ thể thao nam"
            ),
        )
    }
}