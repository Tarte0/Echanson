/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package stev.echanson.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import stev.echanson.R;
import stev.echanson.classes.Classifier.Recognition;

import java.util.Arrays;
import java.util.List;


public class RecognitionScoreView extends View implements ResultsView {
  private static final float TEXT_SIZE_DIP = 24;
  private List<Recognition> results;
  private final float textSizePx;
  private final Paint fgPaint;
  private float[] charWidths = new float[100];
  private float[] textCharWidths;
  private final Paint bgPaint;

  public RecognitionScoreView(final Context context, final AttributeSet set) {
    super(context, set);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setColor(Color.parseColor("#ffffff"));
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
  }

  @Override
  public void setResults(final List<Recognition> results) {
    this.results = results;
    postInvalidate();
  }

  @Override
  public void onDraw(final Canvas canvas) {

    String text;
    float textWidth = 0;
    int x;
    int y = (int) (fgPaint.getTextSize() * 1.5f);

    canvas.drawPaint(bgPaint);

      // we measure the text width to center it
    if (results != null) {
      for (final Recognition recog : results) {
        textWidth = 0;
        text = getText(recog);
        for(int i=0; i<text.length(); i++) {
          charWidths[i] = text.charAt(i);
        }
        textCharWidths = Arrays.copyOfRange(charWidths, 0, text.length());
        fgPaint.getTextWidths(text, 0, (text.length()-1)%100,
                  textCharWidths);
        for(int i=0; i<text.length(); ++i){
          textWidth += textCharWidths[i];
        }
        x = (int) (canvas.getWidth()/2 - textWidth/2);
        y = (int) (canvas.getHeight()/2 + fgPaint.getTextSize()/2);
        canvas.drawText(getText(recog), x, y, fgPaint);

      }
    }
  }

  String getText(Recognition recog){
    return recog.getTitle() + ": " + (int)(recog.getConfidence()*100) + " %";
  }
}
