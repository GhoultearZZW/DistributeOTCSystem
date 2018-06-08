package com.tradergateway.Tools;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetEndTime {
    public Date getEndTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
