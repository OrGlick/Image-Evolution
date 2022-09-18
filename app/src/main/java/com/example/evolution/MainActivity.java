package com.example.evolution;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    private ImageView imageView;
    private Bitmap originalBitmap;
    private int[] originalPixelArray;
    int[][] arrGroup;

    private final int numOfItems = 5;
    private final int waitingMillis = 1;

    private boolean[] correctPlaceHolder;
    private int[] winnerArray;
    private final String tag = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mainFunction();
    }

    private void mainFunction()
    {
        textView.setText("start");
        createArrayGroup(arrGroup);
        findWinner(arrGroup);
        showPixelArray(winnerArray);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDone())
                {
                    textView.setText("Done");
                }
                else
                {
                    mainFunction();
                }
            }
        }, waitingMillis);


    }

    //initialization of all the parts
    private void init()
    {
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text_view);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image9);
        originalPixelArray = new int[originalBitmap.getWidth() * originalBitmap.getHeight()];
        originalBitmap.getPixels(originalPixelArray, 0, originalBitmap.getWidth(), 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
        correctPlaceHolder = new boolean[originalPixelArray.length];
        Arrays.fill(correctPlaceHolder, false);
        winnerArray = new int[originalPixelArray.length];
        arrGroup = new int[numOfItems][originalPixelArray.length];
    }

    //create an array with arrays of pixel inside
    private void createArrayGroup(int[][] arrGroup)
    {
        for(int i = 0; i < arrGroup.length; i++)
        {
            for(int j = 0; j < arrGroup[0].length; j++)
            {
                if(!correctPlaceHolder[j])
                {
                    arrGroup[i][j] = getRandomPixelFromOriginalArray();
                }
                else
                {
                    arrGroup[i][j] = winnerArray[j];
                }
            }
        }
    }

    //returns a random pixel from the original image
    private int getRandomPixelFromOriginalArray()
    {
        Random random = new Random();
        int randomIndex = random.nextInt(originalPixelArray.length);
        return originalPixelArray[randomIndex];
    }

    //find the pixel array which is the closest to the original array
    private void findWinner(int[][] arrGroup)
    {
        int[] count = new int[arrGroup.length];
        boolean[][] placeHolder = new boolean[arrGroup.length][arrGroup[0].length];
        for(int i = 0; i < arrGroup.length; i++)
        {
            for(int j = 0; j < arrGroup[0].length; j++)
            {
                if(arrGroup[i][j] == originalPixelArray[j])
                {
                    count[i]++;
                    placeHolder[i][j] = true;
                }
                else
                {
                    placeHolder[i][j] = false;
                }
            }
        }


        int winnerIndex = findBiggestElementIndex(count);

        double n = count[winnerIndex];
        double pr = n*100/originalPixelArray.length;
        textView.setText("%" + String.valueOf(pr) + ", correct pixels: " + count[winnerIndex]);

        winnerArray = arrGroup[winnerIndex];
        correctPlaceHolder = placeHolder[winnerIndex];
    }

    //returns the biggest element's index of a given array
    private int findBiggestElementIndex(int[] count)
    {
        int max = count[0];
        int maxIndex = 0;
        for (int i = 1; i < count.length; i++)
        {
            if(count[i] > max)
            {
                max = count[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    //show a pixel array on the screen as an image
    private void showPixelArray(int[] pixels)
    {
        Bitmap pixelBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        pixelBitmap.setPixels(pixels, 0, originalBitmap.getWidth(), 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
        imageView.setImageBitmap(pixelBitmap);
    }


    //check if our bitmap is the original image
    private boolean isDone()
    {
        for (int i = 0; i < winnerArray.length; i++)
        {
            if(!(winnerArray[i] == originalPixelArray[i]))
                return false;
        }
        return true;
    }
}