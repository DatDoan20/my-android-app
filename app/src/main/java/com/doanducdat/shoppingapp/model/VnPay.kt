package com.doanducdat.shoppingapp.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class VnPay(val totalPayment: String?) {
    val vnp_TmnCode = "4X1Z2R1L"
    val vnp_HashSecret = "AYHUDKMZGOOMFHUOUWCHSHUJBTZAQMAE"
    val vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"

    fun getUrl(): String {
        val vnp_Version = "2.1.0"
        val vnp_Command = "pay"
        val vnp_OrderInfo = "thanh toán đơn hàng"
        val orderType = "other"
        val vnp_TxnRef = System.currentTimeMillis().toString()
        val vnp_IpAddr = "127.0.0.1"
        val vnp_TmnCode = this.vnp_TmnCode
        val amount: Int = (this.totalPayment?.toInt() ?: 100000) * 100

        val vnp_Params: MutableMap<String, String> = mutableMapOf()
        vnp_Params["vnp_Version"] = vnp_Version
        vnp_Params["vnp_Command"] = vnp_Command
        vnp_Params["vnp_TmnCode"] = vnp_TmnCode
        vnp_Params["vnp_Amount"] = amount.toString()
        vnp_Params["vnp_CurrCode"] = "VND"

        val bank_code: String = ""
        if (bank_code.isNotEmpty()) {
            vnp_Params["vnp_BankCode"] = bank_code
        }
        vnp_Params["vnp_TxnRef"] = vnp_TxnRef
        vnp_Params["vnp_OrderInfo"] = vnp_OrderInfo
        vnp_Params["vnp_OrderType"] = orderType

        val locate: String = ""
        if (locate.isNotEmpty()) {
            vnp_Params["vnp_Locale"] = locate
        } else {
            vnp_Params["vnp_Locale"] = "vn"
        }
        vnp_Params["vnp_ReturnUrl"] = "https%3A%2F%2Fdomainmerchant.vn%2FReturnUrl"
        vnp_Params["vnp_IpAddr"] = vnp_IpAddr

        val cld: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"))
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        val vnp_CreateDate: String = formatter.format(cld.time)
        vnp_Params["vnp_CreateDate"] = vnp_CreateDate

        cld.add(Calendar.MINUTE, 15)
        val vnp_ExpireDate: String = formatter.format(cld.time)
        vnp_Params["vnp_ExpireDate"] = vnp_ExpireDate

        vnp_Params["vnp_Bill_Mobile"] = "0964574166"
        vnp_Params["vnp_Bill_Email"] = "doanducdat271020@gmail.com"
        vnp_Params["vnp_Bill_FirstName"] = "Dat"
        vnp_Params["vnp_Bill_LastName"] = "Doan"

        vnp_Params["vnp_Bill_Address"] = "Bien Hoa"
        vnp_Params["vnp_Bill_City"] = "Bien Hoa"
        vnp_Params["vnp_Bill_Country"] = "Viet Nam"
        vnp_Params["vnp_Bill_State"] = "Viet Nam"

        // Invoice
        vnp_Params["vnp_Inv_Phone"] = "0964574166"
        vnp_Params["vnp_Inv_Email"] = "doanducdat271020@gmail.com"
        vnp_Params["vnp_Inv_Customer"] = "Dat"
        vnp_Params["vnp_Inv_Address"] = "doanducdat271020@gmail.com"
        vnp_Params["vnp_Inv_Company"] = "doanducdat271020@gmail.com"
        vnp_Params["vnp_Inv_Taxcode"] = "doanducdat271020@gmail.com"
//        vnp_Params["vnp_Inv_Type"] = req.getParameter("cbo_inv_type")
        //Build data to hash and querystring
        val fieldNames: List<String> = ArrayList(vnp_Params.keys)
        Collections.sort(fieldNames)
        val hashData = StringBuilder()
        val query = StringBuilder()
        val itr = fieldNames.iterator()
        while (itr.hasNext()) {
            val fieldName = itr.next() as String
            val fieldValue = vnp_Params[fieldName] as String?
            if (fieldValue != null && fieldValue.length > 0) {
                //Build hash data
                hashData.append(fieldName)
                hashData.append('=')
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                query.append('=')
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                if (itr.hasNext()) {
                    query.append('&')
                    hashData.append('&')
                }
            }
        }
        var queryUrl = query.toString()
        val vnp_SecureHash: String = calculateHMAC(this.vnp_HashSecret, hashData.toString())
        queryUrl += "&vnp_SecureHash=$vnp_SecureHash"
        val paymentUrl: String = this.vnp_Url + "?" + queryUrl
        return paymentUrl
//        val job = JsonObject()
//        job.addProperty("code", "00")
//        job.addProperty("message", "success")
//        job.addProperty("data", paymentUrl)
//        val gson = Gson()
//        resp.getWriter().write(gson.toJson(job))
    }

    private fun calculateHMAC(vnpHashSecret: String, hashData: String): String {
        val secretKeySpec = SecretKeySpec(vnpHashSecret.toByteArray(), "HmacSHA512")
        val mac: Mac = Mac.getInstance("HmacSHA512")
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(hashData.toByteArray()));
    }

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }
}