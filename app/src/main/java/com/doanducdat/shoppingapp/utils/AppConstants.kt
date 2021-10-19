package com.doanducdat.shoppingapp.utils

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

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
            const val ADD_TO_CART = 15
            const val PLUSH_PRODUCT_IN_CART = 16
            const val MINUS_PRODUCT_IN_CART = 17
            const val ORDER = 18
            const val VERIFY_EMAIL = 19
            const val NAV_MANAGE_ORDER = 20
            const val CANCEL_ORDER = 21
            const val NAV_DETAIL_ORDER = 22
            const val REVIEW_PRODUCT = 23
            const val NAV_REVIEW_PRODUCT = 24
            const val CREATE_COMMENT = 25
            const val REVIEW_PURCHASED_PRODUCT = 26
            const val NAV_ADD_TO_CARD = 27
            const val READ_NOTIFY_COMMENT = 28
            const val READ_NOTIFY_ORDER = 29
            const val NAV_EDIT_PROFILE = 30
            const val PICKER_PHOTO = 31
            const val SAVE_INFO_PERSONAL = 32
            const val CLICK_SEARCH= 33

            const val SEE_ALL_NEW_PRODUCT = "SEE_ALL_NEW_PRODUCT"
            const val SEE_ALL_SALE_PRODUCT = "SEE_ALL_SALE_PRODUCT"
            const val SEE_PRODUCT_BY_CATEGORY = "SEE_PRODUCT_BY_CATEGORY"
            const val SEE_PRODUCT_BY_SEARCH = "SEE_PRODUCT_BY_SEARCH"
            const val NAME_EVENT = "NAME_EVENT"
        }
    }

    annotation class Server {
        companion object {
            const val PUBLIC_HOST = "https://my-shopping-web-server.herokuapp.com"
            //local host is run in ADV
            const val LOCAL_HOST = "http://10.0.2.2:3000"
        }
    }

    annotation class HeaderRequest {
        companion object {
            const val BEARER = "Bearer"
        }
    }

    annotation class BodyRequest {
        companion object {
            const val NAME = "name"
            const val BIRTH_YEAR = "birthYear"
            const val SEX = "sex"
            const val AVATAR = "avatar"
        }
    }

    annotation class Response {
        companion object {
            const val ERR_JWT_EXPIRED = "TokenExpiredError"
            const val ERR_DUPLICATE = "E11000"
            const val ERR_INCORRECT_PHONE_OR_PASS = "Incorrect"
            const val ERR_LIMIT = "LIMIT"
            const val ERR_DELEGATE_SERVER = "Thất bại"
        }
    }

    annotation class QueryRequest {
        companion object {
            const val LIMIT_10 = 10
            const val LIMIT_5 = 5
            const val LIMIT_8 = 8
            const val PAGE_1 = 1
            const val DISCOUNTING = 0
            const val WAITING = "waiting"
            const val PAYMENT_MODE = "COD"
        }
    }

    annotation class Time {
        companion object {
            private const val SECOND_MILLIS: Int = 1000
            const val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
            const val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
            const val DAY_MILLIS: Int = 24 * HOUR_MILLIS
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

    annotation class TAG() {
        companion object {
            const val LOAD_ME = "LOAD_ME"
            const val BACK_FRAGMENT = "BACK_FRAGMENT"
            const val VERIFY_EMAIL = "VERIFY_EMAIL"
            const val SIGN_UP = "SIGN_UP"
            const val SIGN_IN = "SIGN_IN"
            const val CART = "CART"
            const val ORDER = "ORDER"
            const val ORDER_MANAGEMENT = "ORDER_MANAGEMENT"
            const val REVIEW = "REVIEW"
            const val COMMENT = "COMMENT"
            const val PURCHASED_PRODUCT = "PURCHASED_PRODUCT"
            const val SOCKET_IO = "SOCKET.IO"
            const val ORDER_NOTI = "ORDER_NOTI"
            const val COMMENT_NOTI = "COMMENT_NOTI"
            const val COUNT_NOTI_ORDER = "COUNT_NOTI_ORDER"
            const val COUNT_NOTI_COMMENT = "COUNT_NOTI_COMMENT"
            const val UPDATE_ME = "COUNT_NOTI_COMMENT"
        }
    }

    annotation class ColorHex() {
        companion object {
            const val UN_READ_STATE_NOTIFY = "#261c87"
        }
    }

    annotation class TextTab() {
        companion object {
            const val MAN = "Nam"
            const val WOMAN = "Nữ"
            const val COMMENT = "Bình luận"
            const val ORDER = "Đơn hàng"
            const val ALL_ORDER = "Tất cả đơn hàng"
            const val HANDING_ORDER = "Đang chờ xử lý"
            const val ACCEPTED_ORDER = "Đặt hàng thành công"
            const val RECEIVED_ORDER = "Giao hàng thành công"
            const val CANCELED_ORDER = "Đơn hàng đã hủy"
        }
    }

    annotation class NewNotifyComment() {
        companion object {
            const val NAME = "Thông báo"
            const val CHANNEL = "Thông báo bình luận"
            const val DES = "Thông báo bình luận mới trong đánh giá mà bạn có tham gia bình luận"

            @RequiresApi(Build.VERSION_CODES.N)
            const val IMPORTANT = NotificationManager.IMPORTANCE_DEFAULT
        }
    }

    annotation class StateNotifyOrder() {
        companion object {
            const val NAME = "Thông báo"
            const val CHANNEL = "Thông báo đơn hàng"
            const val DES = "Thông báo trạng thái đơn hàng khi có sự cập nhật"
            const val MSG_ACCEPTED = "Bạn đã đặt hàng thành công đơn hàng:"
            const val MSG_CANCELED = "của bạn đã được hủy, xin lỗi vì sự đáng tiếc này"
            const val MSG_RECEIVED = "Nhận hàng thành công đơn hàng:"
            const val MSG_THANKS =
                "Cảm ơn bạn đã ủng hộ shop. Chúc bạn một ngày tốt lành \uD83D\uDC96"
            const val DETAIL_SUPPORT =
                "Mọi chi tiết thắc mắc xin liên hệ để được hộ trợ. \nFacebook: https://facebook.com/clothesdshop"

            @RequiresApi(Build.VERSION_CODES.N)
            const val IMPORTANT = NotificationManager.IMPORTANCE_DEFAULT

            const val RECEIVER_NAME = "Thông báo đơn hàng"
        }
    }

    annotation class SocketIO {
        companion object {
            const val NEW_COMMENT = "newComment"
            const val STATE_ORDER = "stateOrder"
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

    annotation class EmailAuth() {
        companion object {
            const val PASS_DEFAULT = "Test#123456789"
            const val UPDATE_EMAIL_SUCCESS = "Xác minh và cập nhật email thành công!"
            const val SEND_SUCCESS =
                "Gửi thành công, Kiểm tra các hộp thư email, thư rác để tìm và để xác nhận"
            const val NOT_VERIFY = "Bạn chưa xác nhận, vui lòng xác nhận trong hộp thư của Email"
            const val NOT_SEND =
                "Bạn cần nhấn gửi xác nhận, và xác nhận thư trong email, sau đó vui lòng chọn cập nhật"
        }
    }

    annotation class MsgErr {
        companion object {
            const val GENERIC_ERR_MSG = "Yêu cầu thất bại! đã có lỗi xảy ra, thử lại sau"
            const val LIMIT_ERR_MSG = "Bạn đã yêu cầu quá nhiều lần, vui lòng thử lại 1 giờ sau!"
            const val GENERIC_ERR_RESPONSE = "Đường truyền mạng internet bị gián đoạn, thử lại sau"
            const val PHONE_ERR_LENGTH = "Chiều dài không hợp lệ"
            const val PASSWORD_ERR_LENGTH = "Độ dài ít hơn 8 kí tự, không chứa khoảng trắng"
            const val NAME_ERR_LENGTH = "Độ dài tối đa 50 kí tự"

            const val PASSWORD_ERR_MSG = "Mật khẩu không hợp lệ"
            const val EMAIL_ERR_MSG = "Email không hợp lệ"
            const val PHONE_ERR_MSG = "Số điện thoại không hợp lệ"
            const val NAME_ERR_MSG = "Tên không được để trống, chứa kí đặc biệt tự hoặc số"
            const val BIRTH_YEAR_ERR_MSG = "Bạn chưa chọn giới tính năm sinh"
            const val GENDER_ERR_MSG = "Bạn chưa chọn giới tính"
            const val ADDRESS_ERR_MSG = "Địa chỉ không được để trống"
            const val EXIT_APP = "Nhấn lần nữa để thoát"

            const val MSG_ERR_JWT_EXPIRED =
                "Phiên đăng nhập tự động đã hết, bạn vui lòng đăng nhập lại để sử dụng"
            const val MSG_ERR_DUPLICATE_SIGN_IN =
                "Số điện thoại hoặc email đã được đăng ký với tài khoản khác, vui lòng kiểm tra lại!"
            const val MSG_ERR_AUTO_SIGN_IN =
                "Đăng nhập thất bại! đã có lỗi xảy ra, thử lại sau"
            const val MSG_ERR_INCORRECT_PHONE_OR_PASS =
                "Số điện thoại hoặc mật khẩu không chính xác, vui lòng kiểm tra lại!"

            const val EMPTY_ORDER = "Bạn không có đơn đặt hàng nào"
            const val EMPTY_NOTIFICATION = "Bạn không có thông báo nào"
            const val EMPTY_PURCHASED_PRODUCT = "Bạn không có sản phẩm chưa đánh giá"
            const val EMPTY_PRODUCT_SEARCH = "Không tìm thấy phẩm nào, hãy thử một tìm kiếm khác"
            const val EMPTY_REVIEW = "Chưa có đánh giá nào cho sản phẩm này"
            const val EMPTY_COMMENT = "Chưa có bình luận nào cho sản đánh giá này"

        }
    }

    annotation class MsgInfo {
        companion object {
            const val CONFIRM_DELETE_PRODUCT_IN_CART =
                "Bạn có muốn xóa sản phẩm này khỏi giỏ hàng của bạn?"
            const val PRODUCT_IS_EXIST_IN_CART = "Sản phẩm này đã có trong giỏ hàng!"
            const val COLOR_ERR_MSG = "Vui lòng chọn màu sản phẩm"
            const val SIZE_ERR_MSG = "Vui lòng chọn kích thước sản phẩm"
            const val MSG_INFO_DELETE_PRODUCT_IN_CART = "Xóa thành công sản phẩm khỏi giỏ hàng"
            const val MSG_INFO_EMPTY_CART = "Không có sản phẩm nào trong giỏ hàng!"
            const val MSG_NOT_VERIFY_EMAIl =
                "Bạn cần xác nhận email trước khi đặt hàng, chọn Hồ sơ > xác nhận email"
            const val MSG_ORDER_SUCCESS = "Đặt hàng thành công"
            const val CONFIRM_DELETE_ORDER = "Bạn có chắc chắn muốn hủy đơn hàng này?"
            const val DELETE_ORDER = "Hủy đơn hàng thành công?"
            const val COMMENTS_EMPTY = "Không có bình luận nào cho đánh giá này!"
            const val CONTENT_REPLY_COMMENT_EMPTY = "Nội dung bình luận của bạn đang trống!"
            const val CONTENT_REVIEW_EMPTY = "Nội dung đánh giá của bạn đang trống!"
            const val RATING_EMPTY = "Vui lòng đánh giá với số ngôi sao bạn muốn!"
            const val CLOSE = "Đóng"
            const val PERMISSION_PICK_PHOTO_NOT_GRANTED =
                "Bạn cần cho phép quyền truy cập để tải ảnh"
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

    annotation class Order {
        companion object {
            const val WAITING = "waiting"
            const val ACCEPTED = "accepted"
            const val CANCELED = "canceled"
            const val RECEIVED = "received"
            const val MSG_WAITING = "Đang chờ xử lý"
            const val MSG_ACCEPTED = "Đặt hàng thành công"
            const val MSG_CANCELED = "Đã hủy"
            const val MSG_RECEIVED = "Đã nhận hàng"
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