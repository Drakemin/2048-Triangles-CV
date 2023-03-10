/*
 * *
 *  Created by Dan El Haddidy on 24.02.23 19:36
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 23.02.23 12:22
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.drakemin.game_2048_Triangles;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.drakemin.game_2048_Triangles.DatabaseFiles.TriangleDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {

    public String TAG = "GameLogic";
    ArrayList<int[]> RtL_Rows = new ArrayList<>();
    ArrayList<int[]> LtR_Rows = new ArrayList<>();
    ArrayList<int[]> RtLd_Rows = new ArrayList<>();
    ArrayList<int[]> LtRu_Rows = new ArrayList<>();
    ArrayList<int[]> RtLu_Rows = new ArrayList<>();
    ArrayList<int[]> LtRd_Rows = new ArrayList<>();
    public String RtL = "RtL";
    public String LtR = "LtR";
    public String RtLd = "RtLd";
    public String LtRu = "LtRu";
    public String RtLu = "RtLu";
    public String LtRd = "LtRd";
    private SharedPreferences sharedPreferences;
    Context context;


    public GameLogic(Context context) {
        populateIndexes();
        this.context = context;
    }

    public void populateIndexes() {

        //(RtL)From right to left swipes indexes
        int[] idsRow1RtL = {1, 2, 3}; // t21,t22,t23
        int[] idsRow2RtL = {4, 5, 6, 7, 8}; // t31,t32,t33,t34,t35
        int[] idsRow3RtL = {9, 10, 11, 12, 13, 14, 15}; //t41,t42,t43,t44,t45,t46,t47
        Collections.addAll(RtL_Rows, idsRow1RtL, idsRow2RtL, idsRow3RtL);

        //(LtR)From right to left swipes indexes
        int[] idsRow1LtR = {3, 2, 1}; // t23,t22,t21
        int[] idsRow2LtR = {8, 7, 6, 5, 4,}; // t35,t34,t33,t32,t31
        int[] idsRow3LtR = {15, 14, 13, 12, 11, 10, 9}; // t47,t46,t45,t44,t43,t42,t41
        Collections.addAll(LtR_Rows, idsRow1LtR, idsRow2LtR, idsRow3LtR);

        //(RtLd)From right to left down swipes indexes
        int[] idsRow1RtLd = {9, 10, 4, 5, 1, 2, 0}; // t41,t42,t31,t32,t21,t22,t11
        int[] idsRow2RtLd = {11, 12, 6, 7, 3}; //t43,t44,t33,t34,t23
        int[] idsRow3RtLd = {13, 14, 8};// t45,t46,t35
        Collections.addAll(RtLd_Rows, idsRow1RtLd, idsRow2RtLd, idsRow3RtLd);

        //(LtRu)Left to right up swipes indexes
        int[] idsRow1LtRu = {0, 2, 1, 5, 4, 10, 9}; // t11,t22,t21,t32,t31,t42,t41
        int[] idsRow2LtRu = {3, 7, 6, 12, 11}; // t23,t34,t33,t44,t43
        int[] idsRow3LtRu = {8, 14, 13};// t35,t46,t45
        Collections.addAll(LtRu_Rows, idsRow1LtRu, idsRow2LtRu, idsRow3LtRu);

        //(RtLu)Right to left up swipe indexes
        int[] idsRow1RtLu = {0, 2, 3, 7, 8, 14, 15}; // t11,t22,t23,t34,t35,t46,t47
        int[] idsRow2RtLu = {1, 5, 6, 12, 13}; // t21,t32,t33,t44,t45
        int[] idsRow3RtLu = {4, 10, 11}; // t31,t42,t43
        Collections.addAll(RtLu_Rows, idsRow1RtLu, idsRow2RtLu, idsRow3RtLu);

        //(LtRd)Left to right down swipe indexes
        int[] idsRow1LtRd = {15, 14, 8, 7, 3, 2, 0}; // t47,t46,t35,t34,t23,t22,t11
        int[] idsRow2LtRd = {13, 12, 6, 5, 1}; // t45,t44,t33,t32,t21
        int[] idsRow3LtRd = {11, 10, 4}; // t43,t42,t31
        Collections.addAll(LtRd_Rows, idsRow1LtRd, idsRow2LtRd, idsRow3LtRd);
    }

    public void checkIfGameLost() {
        if (!canMove(RtL) && !canMove(LtR) && !canMove(RtLd) && !canMove(LtRu) && !canMove(RtLu) && !canMove(LtRd)) {
            GameActivity.overBGameOver.setVisibility(View.VISIBLE);
            GameActivity.overBGameOver.getBackground().setAlpha(50);
            settingHighestScore();
            sharedPreferences = context.getSharedPreferences("2048x3_shared_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            editor.putBoolean("gameLost", true);
            editor.apply();

        }

    }


    public void addingOfTriangles(String typeOfSwipe) {


        ArrayList<int[]> rows = assignRowsToSwipe(typeOfSwipe);
        ArrayList<ArrayList<Integer>> idsWithValueMatching = checkIfValueMatching(rows);

        sharedPreferences = context.getSharedPreferences("2048x3_shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        for (ArrayList<Integer> r : idsWithValueMatching) {
            if (r.size() == 2) {

                updateFieldValue(r.get(0), getFieldValue(r.get(0)) * 2);
                updateFieldValue(r.get(1), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(0)));
                editor.apply();


            } else if (r.size() == 4) {

                updateFieldValue(r.get(0), getFieldValue(r.get(0)) * 2);
                updateFieldValue(r.get(1), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(0)));


                updateFieldValue(r.get(2), getFieldValue(r.get(2)) * 2);
                updateFieldValue(r.get(3), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(2)));
                editor.apply();

            } else if (r.size() == 6) {

                updateFieldValue(r.get(0), getFieldValue(r.get(0)) * 2);
                updateFieldValue(r.get(1), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(0)));

                updateFieldValue(r.get(2), getFieldValue(r.get(2)) * 2);
                updateFieldValue(r.get(3), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(2)));

                updateFieldValue(r.get(4), getFieldValue(r.get(4)) * 2);
                updateFieldValue(r.get(5), 0);
                editor.putInt("score", sharedPreferences.getInt("score", 0) + getFieldValue(r.get(4)));
                editor.apply();

            }
        }

        Log.d(TAG, "addingOfTriangles");

    }

    public ArrayList checkIfValueMatching(ArrayList<int[]> rows) {


        ArrayList<ArrayList<Integer>> idsWithValueMatching = new ArrayList<>();
        ArrayList<Integer> row1 = new ArrayList<>();
        ArrayList<Integer> row2 = new ArrayList<>();
        ArrayList<Integer> row3 = new ArrayList<>();
        Collections.addAll(idsWithValueMatching, row1, row2, row3);

        int currentRow = 0;

        for (int[] r : rows) {
            for (int i = 0; i < r.length - 1; i++) {
                if (getFieldValue(r[i]) != 0) {
                    if (getFieldValue(r[i]) == getFieldValue(r[i + 1])) {
                        // to this index number will be added
                        idsWithValueMatching.get(currentRow).add(r[i]);
                        // to this index 0 will be added
                        idsWithValueMatching.get(currentRow).add(r[i + 1]);
                        i += 1;
                    }
                }
            }
            currentRow++;
        }

        Log.d(TAG, "checkIfValueMatching");

        return idsWithValueMatching;
    }


    public ArrayList<ArrayList<Integer>> movingValuesToEmptyFieldsAfterAdding(String typeOfSwipe,ArrayList<ArrayList<Integer>> beforeAddingMatchingValues ) {

        ArrayList<ArrayList<Integer>> afterMovingMatchingValues=beforeAddingMatchingValues;


        ArrayList<int[]> rows = assignRowsToSwipe(typeOfSwipe);
        int rowNumber = -1;
        for (int[] r : rows) {
            rowNumber++;
            int firstIdsZeroValue = -1;
            boolean firstZero = false;



            // You can improve it by lowering the number of cycles after you move one number
            for (int i = 0; i < r.length; i++) {

                if (!firstZero) {
                    if (getFieldValue(r[i]) == 0) {
                        firstIdsZeroValue = r[i];
                        firstZero = true;
                    }
                }
                if (firstZero && firstIdsZeroValue != r[i]) {

                    if (getFieldValue(r[i]) != 0) {
                        updateFieldValue(firstIdsZeroValue, getFieldValue(r[i]));
                        updateFieldValue(r[i], 0);

                        for (int j = 0; j <beforeAddingMatchingValues.get(rowNumber).size() ; j+=2) {
                            if(beforeAddingMatchingValues.get(rowNumber)!=null) {
                                if (beforeAddingMatchingValues.get(rowNumber).get(j) == r[i]) {
                                    afterMovingMatchingValues.get(rowNumber).set(j, firstIdsZeroValue);
                                }
                            }

                        }


                        firstZero = false;
                        firstIdsZeroValue = -1;
                        i = 0; // by lowering this i after every cycle
                    }
                }

            }
        }

        Log.d(TAG, "movingValuesToEmptyFields");
        return afterMovingMatchingValues;
    }

    public ArrayList<ArrayList<Integer>> movingValuesToEmptyFields(String typeOfSwipe) {


        ArrayList<int[]> rows = assignRowsToSwipe(typeOfSwipe);

        // here is stored first and last ids of moved value
        ArrayList<Integer> idsMoved = new ArrayList<>();

        int row = 0;
        for (int[] r : rows) {

            int firstIdsZeroValue = -1;
            int firstIdsBeforeZeroValue = -1;
            boolean firstZero = false;


            // You can improve it by lowering the number of cycles after you move one number
            for (int i = 0; i < r.length; i++) {

                if (!firstZero) {
                    if (getFieldValue(r[i]) == 0) {
                        firstIdsZeroValue = r[i];
                        if (r.length > (i + 1)) {
                            firstIdsBeforeZeroValue = r[i + 1];
                        }
                        firstZero = true;

                    }
                }
                if (firstZero && firstIdsZeroValue != r[i]) {

                    if (getFieldValue(r[i]) != 0) {

                        idsMoved.add(r[i]);
                        idsMoved.add(firstIdsBeforeZeroValue);
                        idsMoved.add(getFieldValue(r[i]));
                        idsMoved.add(row);
                        idsMoved.add(firstIdsZeroValue);
                        

                        updateFieldValue(firstIdsZeroValue, getFieldValue(r[i]));
                        updateFieldValue(r[i], 0);
                        firstZero = false;
                        firstIdsZeroValue = -1;
                        i = 0; // by lowering this i after every cycle

                    }
                }

            }
            row++;
        }
        
        ArrayList<ArrayList<Integer>> idsPaths = new ArrayList<>();

        if (idsMoved.size() > 0) {
            for (int i = 0; i < idsMoved.size(); i += 5) {
                ArrayList<Integer> idsPath = new ArrayList<>();
                // add first id
                idsPath.add(idsMoved.get(i));
                Boolean lastIds = false;
                Boolean firstId = false;
                int[] r = rows.get(idsMoved.get(i + 3));

                for (int j = r.length - 1; j > -1; j--) {

                    if (r[j] == idsMoved.get(i)) {
                        firstId = true;
                    }
                    if (r[j] == idsMoved.get(i + 1)) {
                        lastIds = true;
                    }
                    if (r[j] != idsMoved.get(i) && firstId == true && lastIds != true) {
                        idsPath.add(r[j]);
                        idsPath.add(r[j]);
                    }
                }

                // add last id
                idsPath.add(idsMoved.get(i + 1));
                idsPath.add(idsMoved.get(i + 1));
                // add value
                idsPath.add(idsMoved.get(i + 2));
                //add idsZero
                idsPath.add(idsMoved.get(i+4));

                idsPaths.add(idsPath);

            }
            return idsPaths;
        }
        Log.d(TAG, "movingValuesToEmptyFields");
        return null;
    }

    public boolean canMove(String typeOfSwipe) {


        ArrayList<int[]> rows = assignRowsToSwipe(typeOfSwipe);

        // Field can be moved because you can add two fields together
        int currentRow = 0;

        for (int[] r : rows) {
            for (int i = 0; i < r.length - 1; i++) {
                if (getFieldValue(r[i]) != 0) {
                    if (getFieldValue(r[i]) == getFieldValue(r[i + 1])) {
                        return true;
                    }
                }
            }
            currentRow++;
        }

        //Field can be moved because there is space in the direction of swipe
        for (int[] r : rows) {

            int firstIdsZeroValue = -1;
            boolean firstZero = false;

            for (int i = 0; i < r.length; i++) {

                if (!firstZero) {
                    if (getFieldValue(r[i]) == 0) {
                        firstIdsZeroValue = r[i];
                        firstZero = true;
                    }
                }
                if (firstZero && firstIdsZeroValue != r[i]) {

                    if (getFieldValue(r[i]) != 0) {
                        return true;
                    }
                }
            }
        }
        Log.d(TAG, "canMove:");
        return false;
    }

    public int add2ToRandomSpot() {

        int twoToId = 0;

        ArrayList<Integer> prob = new ArrayList<>();
        Collections.addAll(prob,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4);

        ArrayList<TriangleField> noValueTriangles = new ArrayList<>();

        for (int i = 0; i < getAllTriangleFields().size(); i++) {
            if (getFieldValue(i) == 0) {
                noValueTriangles.add(getTriangleField(i));
            }
        }
        Collections.shuffle(noValueTriangles);
        if (!noValueTriangles.isEmpty()) {
            TriangleField t = noValueTriangles.get(0);
            for (int i = 0; i < getAllTriangleFields().size(); i++) {
                if (i == t.getId()) {
                    Collections.shuffle(prob);
                    updateFieldValue(i, prob.get(0));
                    twoToId = i;
                }
            }
        }

        Log.d(TAG, "add2ToRandomSpot");

        return twoToId;
    }

    private void settingHighestScore() {

        sharedPreferences = context.getSharedPreferences("2048x3_shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int spHighScore = sharedPreferences.getInt("highest_score", 0);
        int score = sharedPreferences.getInt("score", 0);

        if (spHighScore < score && sharedPreferences.getBoolean("gameOver", false) == true) {
            editor.putInt("highest_score", score);
            editor.apply();
        }
    }

    public ArrayList<int[]> assignRowsToSwipe(String typeOfSwipe) {
        ArrayList<int[]> rows = new ArrayList<>();

        switch (typeOfSwipe) {
            case "RtL":
                rows = RtL_Rows;
                break;
            case "LtR":
                rows = LtR_Rows;
                break;
            case "RtLd":
                rows = RtLd_Rows;
                break;
            case "LtRu":
                rows = LtRu_Rows;
                break;
            case "RtLu":
                rows = RtLu_Rows;
                break;
            case "LtRd":
                rows = LtRd_Rows;
                break;
        }
        return rows;
    }

    //SQL methods shortened :(?!
    // getAllTriangleField is done
    public ArrayList<TriangleField> getAllTriangleFields() {
        return (ArrayList<TriangleField>) TriangleDatabase.getInstance(context).triangleFieldDao().getAllTriangleFields();
    }

    public TriangleField getTriangleField(int id) {
        return TriangleDatabase.getInstance(context).triangleFieldDao().getTriangleField(id);
    }

    public void updateFieldValue(int id, int newFieldValue) {
        TriangleDatabase.getInstance(context).triangleFieldDao().updateFieldValue(id, newFieldValue);
    }

    public int getFieldValue(int id) {
        return (int) TriangleDatabase.getInstance(context).triangleFieldDao().getFieldValue(id);
    }

}
