package com.doanducdat.shoppingapp.utils

object AppConstants {

    annotation class ActionClick {
        companion object {
            const val SIGN_IN = 1
            const val NAV_SIGN_UP = 2
            const val NAV_FORGOT = 3
            const val SIGN_UP = 4
            const val NAV_SIGN_IN = 5
            const val VERIFY_OTP = 6
        }
    }

    annotation class RegexCheck {
        companion object {
            const val CONTAINS_NUMBER = ".*\\d+.*"
            const val CONTAINS_SPECIAL_CHARACTER = "[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]"
        }
    }

    annotation class PhoneAuth {
        companion object {
            const val LANGUAGE_CODE_VN = "vi"
            const val INVALID_CREDENTIALS = "Yêu cầu gửi đi không hợp lệ!"
            const val TOO_MANY_REQUESTS = "Yêu cầu gửi đi quá nhiều lần, thử lại sau!"
            const val VERIFY_OTP_MSG = "Xác nhận thành công"
        }
    }


    annotation class MsgError {
        companion object {
            const val GENERIC_ERR_MSG = "Đã có lỗi xảy ra, thử lại sau!"
            const val PHONE_ERR_LENGTH = "Chiều dài không hợp lệ"
            const val PASSWORD_ERR_LENGTH = "Độ dài ít hơn 8 kí tự, không chứa khoảng trắng"
            const val NAME_ERR_LENGTH = "Độ dài tối đa 50 kí tự"

            const val PASSWORD_ERR_MSG = "Mật khẩu không hợp lệ"
            const val EMAIL_ERR_MSG = "Email không hợp lệ"
            const val PHONE_ERR_MSG = "Số điện thoại không hợp lệ"
            const val NAME_ERR_MSG = "Tên không được chứa kí đặc biệt tự hoặc số"
            const val NAME_ERR_MSG_EMPTY = "Tên không hợp lệ"
            const val OTP_ERR_MSG_EMPTY = "Vui lòng nhập mã OTP để xác nhận số điện thoại!"
            const val OTP_ERR_MSG_UN_VALID = "Mã OTP không hợp lệ!"
            const val OTP_ERR_MSG_NOT_ENOUGH = "Mã OTP phải đủ 6 số"
        }
    }

    annotation class ContentOnboard {
        companion object {
            const val TITLE_FIRST_FRAGMENT = "Cập nhật nhanh chóng"
            const val DES_FIRST_FRAGMENT =
                "Thông tin sản phẩm luôn được cập nhật hàng ngày trên ứng dụng"
            const val TITLE_SECOND_FRAGMENT = "Giao hàng tiện lợi"
            const val DES_SECOND_FRAGMENT = "Giao hàng an toàn và nhanh chóng khi có đơn đặt hàng"
            const val TITLE_THIRD_FRAGMENT = "Bảo mật thông tin"
            const val DES_THIRD_FRAGMENT =
                "Thông tin của người dùng luôn được bảo mật, tránh rò rỉ thông tin"
        }
    }

}