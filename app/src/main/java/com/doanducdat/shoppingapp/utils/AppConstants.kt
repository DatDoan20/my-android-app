package com.doanducdat.shoppingapp.utils

object AppConstants {
    annotation class ActionClick{
        companion object{
            const val LOGIN = 1
            const val NAV_SIGNUP = 2
            const val NAV_FORGOT = 3
        }
    }
    annotation class ContentOnboard {
        companion object{
            const val TITLE_FIRST_FRAGMENT = "Cập nhật nhanh chóng"
            const val DES_FIRST_FRAGMENT = "Thông tin sản phẩm luôn được cập nhật hàng ngày trên ứng dụng"
            const val TITLE_SECOND_FRAGMENT = "Giao hàng tiện lợi"
            const val DES_SECOND_FRAGMENT = "Giao hàng an toàn và nhanh chóng khi có đơn đặt hàng"
            const val TITLE_THIRD_FRAGMENT = "Bảo mật thông tin"
            const val DES_THIRD_FRAGMENT = "Thông tin của người dùng luôn được bảo mật, tránh rò rỉ thông tin"
        }
    }
    annotation class MsgError{
        companion object{
            const val PHONE_ERR_LENGTH = "Chiều dài không hợp lệ"
            const val PASSWORD_ERR_LENGTH = "Độ dài ít hơn 8 kí tự, không chứa khoảng trắng"
            const val PASSWORD_ERR_MSG = "Mật khẩu không hợp lệ"
            const val PHONE_ERR_MSG = "Số điện thoại không hợp lệ"
        }
    }
}