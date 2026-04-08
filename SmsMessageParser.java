package com.kroot.connect.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * فئة تحليل رسائل SMS واستخراج البيانات باستخدام الذكاء الاصطناعي البسيط
 */
public class SmsMessageParser {

    /**
     * استخراج المبلغ من رسالة SMS
     * يبحث عن أنماط مثل "أضيف 100ر.ي" أو "أضيف 100 ر.ي"
     * الفئات المقبولة فقط: 100، 200، 300، 500
     */
    public static Integer extractAmount(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }

        // الأنماط المحتملة للمبلغ
        String[] patterns = {
            "أضيف\\s*(\\d+)\\s*ر\\.?ي",  // أضيف 100ر.ي أو أضيف 100 ر.ي
            "تم\\s*إضافة\\s*(\\d+)",      // تم إضافة 100
            "المبلغ\\s*(\\d+)",            // المبلغ 100
            "\\*\\*\\s*(\\d+)\\s*\\*\\*",  // **100**
            "^(\\d+)$"                     // فقط الرقم
        };

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                try {
                    int amount = Integer.parseInt(matcher.group(1));
                    // التحقق من أن المبلغ من الفئات المقبولة فقط
                    if (isValidCategory(amount)) {
                        return amount;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        return null;
    }

    /**
     * التحقق من أن المبلغ ينتمي إلى الفئات المقبولة
     * الفئات المقبولة: 100، 200، 300، 500
     */
    private static boolean isValidCategory(int amount) {
        return amount == 100 || amount == 200 || amount == 300 || amount == 500;
    }

    /**
     * استخراج رقم العميل من رسالة SMS
     * يبحث عن أرقام الهاتف بصيغ مختلفة
     */
    public static String extractCustomerNumber(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }

        // الأنماط المحتملة لرقم الهاتف
        String[] patterns = {
            "من\\s*([0-9]{8,})",          // من 912345678
            "رقم\\s*([0-9]{8,})",         // رقم 912345678
            "الهاتف\\s*([0-9]{8,})",      // الهاتف 912345678
            "([0-9]{8,})",                // أي رقم بـ 8 أرقام فأكثر
            "\\+?[0-9]{10,}"              // رقم دولي
        };

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String number = matcher.group(1);
                // تنظيف الرقم
                number = number.replaceAll("[^0-9]", "");
                if (number.length() >= 8) {
                    return number;
                }
            }
        }

        return null;
    }

    /**
     * التحقق من أن الرسالة تحتوي على عبارة "تحويل مشترك"
     */
    public static boolean isSubscriberTransfer(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }

        String[] keywords = {
            "تحويل مشترك",
            "تحويل",
            "مشترك",
            "subscriber",
            "transfer"
        };

        String lowerMessage = message.toLowerCase();
        for (String keyword : keywords) {
            if (lowerMessage.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * التحقق من أن الرسالة تحتوي على عبارة "أضيف"
     */
    public static boolean containsAddKeyword(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }

        String[] keywords = {
            "أضيف",
            "تم إضافة",
            "تمت إضافة",
            "تم تحويل",
            "تمت تحويل"
        };

        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    /**
     * التحقق من أن الرسالة من مرسل محدد
     */
    public static boolean isFromSender(String senderName, String senderAddress) {
        if (senderName == null || senderAddress == null) {
            return false;
        }

        return senderAddress.equalsIgnoreCase(senderName) || 
               senderAddress.contains(senderName);
    }

    /**
     * تحليل شامل للرسالة
     */
    public static SmsAnalysisResult analyzeSms(String message, String senderAddress, String expectedSender) {
        SmsAnalysisResult result = new SmsAnalysisResult();

        // التحقق من المرسل
        if (!isFromSender(expectedSender, senderAddress)) {
            result.isValid = false;
            result.reason = "المرسل غير متطابق";
            return result;
        }

        // التحقق من الكلمات الأساسية
        if (!containsAddKeyword(message) || !isSubscriberTransfer(message)) {
            result.isValid = false;
            result.reason = "الرسالة لا تحتوي على الكلمات المطلوبة";
            return result;
        }

        // استخراج البيانات
        Integer amount = extractAmount(message);
        String customerNumber = extractCustomerNumber(message);

        if (amount == null) {
            result.isValid = false;
            result.reason = "لم يتمكن من استخراج المبلغ";
            return result;
        }

        if (customerNumber == null) {
            result.isValid = false;
            result.reason = "لم يتمكن من استخراج رقم العميل";
            return result;
        }

        result.isValid = true;
        result.amount = amount;
        result.customerNumber = customerNumber;
        result.reason = "تم التحليل بنجاح";

        return result;
    }

    /**
     * فئة لتخزين نتائج التحليل
     */
    public static class SmsAnalysisResult {
        public boolean isValid;
        public Integer amount;
        public String customerNumber;
        public String reason;

        @Override
        public String toString() {
            return "SmsAnalysisResult{" +
                    "isValid=" + isValid +
                    ", amount=" + amount +
                    ", customerNumber='" + customerNumber + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }
}
