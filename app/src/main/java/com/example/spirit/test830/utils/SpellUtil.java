package com.example.spirit.test830.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class SpellUtil {
    public static String getSpell(String word) {
        if (TextUtils.isEmpty(word)) return null;

        //用来设置转化的拼音的大小写，或者声调
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        char[] chars = word.toCharArray();
        String spellWord = "";
        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) continue;

            if (chars[i] > 127) {
                //可能是汉字
                try {
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (pinyinArr != null) {
                        spellWord += pinyinArr[0];
                    } else {
                        //说明没有找到对应的拼音，汉字有问题，忽略它
                    }
                } catch (BadHanyuPinyinOutputFormatCombination
                        badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            } else {
                spellWord += chars[i];
            }
        }

        return spellWord;
    }
}
