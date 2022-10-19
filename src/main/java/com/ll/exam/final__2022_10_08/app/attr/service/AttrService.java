package com.ll.exam.final__2022_10_08.app.attr.service;

import com.ll.exam.final__2022_10_08.app.attr.entity.Attr;
import com.ll.exam.final__2022_10_08.app.attr.repository.AttrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AttrService {
    private final AttrRepository attrRepository;

    @Transactional
    public void set(String varName, String value) {
        set(varName, value, null);
    }

    @Transactional
    public void set(String varName, String value, LocalDateTime expireDate) {
        String[] varNameBits = varName.split("__");
        String relTypeCode = varNameBits[0];
        long relId = Integer.parseInt(varNameBits[1]);
        String typeCode = varNameBits[2];
        String type2Code = varNameBits[3];

        attrRepository.upsert(relTypeCode, relId, typeCode, type2Code, value, expireDate);
    }

    @Transactional
    public void set(String varName, long value) {
        set(varName, String.valueOf(value));
    }

    @Transactional
    public void set(String varName, long value, LocalDateTime expireDate) {
        set(varName, String.valueOf(value), expireDate);
    }

    @Transactional
    public void set(String varName, boolean value) {
        set(varName, String.valueOf(value));
    }

    @Transactional
    public void set(String varName, boolean value, LocalDateTime expireDate) {
        set(varName, String.valueOf(value), expireDate);
    }

    public Attr findAttr(String varName) {
        String[] varNameBits = varName.split("__");
        String relTypeCode = varNameBits[0];
        long relId = Integer.parseInt(varNameBits[1]);
        String typeCode = varNameBits[2];
        String type2Code = varNameBits[3];

        return attrRepository.findByRelTypeCodeAndRelIdAndTypeCodeAndType2Code(relTypeCode, relId, typeCode, type2Code).orElse(null);
    }

    public String get(String varName, String defaultValue) {
        Attr attr = findAttr(varName);

        if (attr == null) {
            return defaultValue;
        }

        if (attr.getExpireDate() != null && attr.getExpireDate().compareTo(LocalDateTime.now()) < 0) {
            return defaultValue;
        }

        return attr.getValue();
    }

    public long getAsLong(String varName, long defaultValue) {
        String value = get(varName, "");

        if (value.equals("")) {
            return defaultValue;
        }

        return Long.parseLong(value);
    }

    public boolean getAsBoolean(String varName, boolean defaultValue) {
        String value = get(varName, "");

        if (value.equals("")) {
            return defaultValue;
        }

        if (value.equals("true")) {
            return true;
        } else return value.equals("1");
    }
}
