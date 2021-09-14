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
            const val NAV_CATEGORY = 7
            const val SELECT_SHIRT = 8
            const val SELECT_DRESS = 9
            const val SELECT_TROUSER = 10
            const val SELECT_HOME_WEAR = 11
            const val SELECT_COAT = 12
            const val SELECT_SPORT_WEAR = 13
            const val SEARCH_VIEW = 14
            const val ADD_TO_CART= 15
            const val PLUSH_PRODUCT_IN_CART = 16
            const val MINUS_PRODUCT_IN_CART = 17
        }
    }

    annotation class Server {
        companion object {
            const val HOST = "http://10.0.2.2:3000"
        }
    }

    annotation class HeaderRequest {
        companion object {
            const val BEARER = "Bearer"
        }
    }

    annotation class QueryRequest {
        companion object {
            const val LIMIT_10 = 10
            const val LIMIT_12 = 8
            const val PAGE_1 = 1
            const val DISCOUNTING = 0
        }
    }

    annotation class LinkImg {
        companion object {
            const val SALE =
                "https://firebasestorage.googleapis.com/v0/b/shoppingapp-7a337.appspot.com/o/shop%2Fevent_small%2Fsale_small.jpg?alt=media&token=68947f12-ad6c-4e89-8a63-89bcbc7214ed"
            const val SPRING =
                "https://firebasestorage.googleapis.com/v0/b/shoppingapp-7a337.appspot.com/o/shop%2Fevent_small%2Fspring_small.jpg?alt=media&token=6e417621-73c1-4fc2-b89c-c687ab63c7be"
            const val SUMMER =
                "https://firebasestorage.googleapis.com/v0/b/shoppingapp-7a337.appspot.com/o/shop%2Fevent_small%2Fsummer_small.jpg?alt=media&token=73abb6ed-4524-45c0-a47d-b8fa1c14fc68"
            const val AUTUMN =
                "https://firebasestorage.googleapis.com/v0/b/shoppingapp-7a337.appspot.com/o/shop%2Fevent_small%2Fautumn_small.jpg?alt=media&token=9437d6f2-5d38-47d9-af5f-0e63f1da9cbb"

            /*** {Host}/img/products/{idProduct}/{nameImg} */
            const val PRODUCT = "/img/products/"

            /*** {Host}/img/users/{nameImg} */
            const val USER = "/img/users/"
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
            const val VERIFY_OTP_MSG_SUCCESS = "Đăng ký Thành công! bạn có thể đăng nhập"
            const val CODE_SENT_SUCCESS = "Gửi mã OTP thành công, vui chờ và kiểm tra tin nhắn"
            const val OTP_ERR_MSG_EMPTY = "Vui lòng nhập mã OTP để xác nhận số điện thoại!"
            const val OTP_ERR_MSG_UN_VALID = "Mã OTP không hợp lệ!"
            const val OTP_ERR_MSG_NOT_ENOUGH = "Mã OTP phải đủ 6 số"
        }
    }

    annotation class MsgErr {
        companion object {
            const val GENERIC_ERR_MSG = "Thất bại! đã có lỗi xảy ra, thử lại sau"
            const val GENERIC_ERR_RESPONSE = "Đường truyền mạng internet bị gián đoạn, thử lại sau"
            const val PHONE_ERR_LENGTH = "Chiều dài không hợp lệ"
            const val PASSWORD_ERR_LENGTH = "Độ dài ít hơn 8 kí tự, không chứa khoảng trắng"
            const val NAME_ERR_LENGTH = "Độ dài tối đa 50 kí tự"

            const val PASSWORD_ERR_MSG = "Mật khẩu không hợp lệ"
            const val EMAIL_ERR_MSG = "Email không hợp lệ"
            const val PHONE_ERR_MSG = "Số điện thoại không hợp lệ"
            const val NAME_ERR_MSG = "Tên không được để trống, chứa kí đặc biệt tự hoặc số"
            const val EXIT_APP = "Nhấn lần nữa để thoát"
            const val COLOR_ERR_MSG = "Vui lòng chọn màu sản phẩm"
            const val SIZE_ERR_MSG = "Vui lòng chọn kích thước sản phẩm"
            const val PRODUCT_IS_EXIST_IN_CART= "Sản phẩm này đã có trong giỏ hàng!"
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

    /**
     * this Type value string is "Type" field stored in DATABASE
     */
    annotation class Type {
        companion object {
            const val VAY_SUONG = "vay-suong"
            const val CHAN_VAY = "chan-vay"
            const val AO_THUN_TAY_DAI = "ao-thun-tay-dai"
            const val AO_SO_MI_TAY_DAI = "ao-so-mi-tay-dai"
            const val AO_THUN_TAY_NGAN = "ao-thun-tay-ngan"
            const val AO_SO_MI_TAY_NGAN = "ao-so-mi-tay-ngan"
            const val QUAN_THUN_DAI = "quan-thun-dai"
            const val QUAN_THUN_NGAN = "quan-thun-ngan"
            const val DO_BO = "do-bo"
            const val AO_KHOAC = "ao-khoac"
            const val DO_THE_THAO = "do-the-thao"
            const val NON = "non"
        }
    }

    /**
     * this Category value string is "Type" field stored in DATABASE
     */
    annotation class Category {
        companion object {
            const val NamAndNu = "nam-nu"
            const val NAM = "nam"
            const val NU = "nu"
        }
    }
}