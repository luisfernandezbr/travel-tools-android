package br.com.mobiplus.eventsender;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

/**
 * Created by luisfernandez on 13/11/17.
 */

public class AnswersEventSender
{
    public static void sendViewProductCalcDetailsScreenEvent() {
        Answers.getInstance().logCustom(new CustomEvent("View_ProductCalcDetails_Screen"));
    }

    public static void sendViewProductCalcScreenEvent()
    {
        Answers.getInstance().logCustom(new CustomEvent("View_ProductCalc_Screen"));
    }
}
