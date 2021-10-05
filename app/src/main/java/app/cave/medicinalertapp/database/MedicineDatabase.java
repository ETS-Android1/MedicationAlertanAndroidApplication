package app.cave.medicinalertapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.cave.medicinalertapp.database.ConstansDatabase;
import app.cave.medicinalertapp.model.MedicineModel;

public class MedicineDatabase extends SQLiteOpenHelper {

    Context context;

    private static final String DATAABASE_NAME = "medicine_alerts";
    private static final int DATABASE_VERSION = 1;

    private static final String BEFORE_MEAL_TABLE = "before_table";
    private static final String AFTER_MEAL_TABLE = "after_table";

    private static final String COLUMN_0 = "ID";
    private static final String COLUMN_1 = "DATE";
    private static final String COLUMN_2 = "MEDICINE_NAME";
    private static final String COLUMN_3 = "MEDICINE_TYPE";
    private static final String COLUMN_4 = "IMAGE_PATH";
    private static final String COLUMN_5 = "NO_OF_SLOT";
    private static final String COLUMN_6 = "FIRST_SLOT";
    private static final String COLUMN_7 = "SECOND_SLOT";
    private static final String COLUMN_8 = "THIRD_SLOT";
    private static final String COLUMN_9 = "NUMBER_OF_DAYS";
    private static final String COLUMN_10 = "IS_EVERYDAY";
    private static final String COLUMN_11 = "IS_SPECIFIC_DAYS_OF_WEEK";
    private static final String COLUMN_12 = "IS_DAYS_INTERVAL";
    private static final String COLUMN_13 = "DAYS_NAME_OF_WEEK";
    private static final String COLUMN_14 = "DAYS_iNTERVAL";
    private static final String COLUMN_15 = "START_DATE";
    private static final String COLUMN_16 = "STATUS";
    private static final String COLUMN_17 = "MEDICINE_MEAL";
    private static final String COLUMN_18 = "UNIQUE_CODE";

    public MedicineDatabase(Context context) {
        super(context, DATAABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String BEFORE_MEAL_QUERY =
                "CREATE TABLE "
                        + BEFORE_MEAL_TABLE + "("
                        + COLUMN_0 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        + COLUMN_1 + " TEXT,"
                        + COLUMN_2 + " TEXT,"
                        + COLUMN_3 + " TEXT,"
                        + COLUMN_4 + " TEXT,"
                        + COLUMN_5 + " INTEGER,"
                        + COLUMN_6 + " TEXT,"
                        + COLUMN_7 + " TEXT,"
                        + COLUMN_8 + " TEXT,"
                        + COLUMN_9 + " INTEGER,"
                        + COLUMN_10 + " BOOLEAN,"
                        + COLUMN_11 + " BOOLEAN,"
                        + COLUMN_12 + " BOOLEAN,"
                        + COLUMN_13 + " TEXT,"
                        + COLUMN_14 + " INTEGER,"
                        + COLUMN_15 + " TEXT,"
                        + COLUMN_16 + " TEXT,"
                        + COLUMN_17 + " TEXT,"
                        + COLUMN_18 + " INTEGER" + ")";

        String AFTER_MEAL_QUERY = "CREATE TABLE "
                + AFTER_MEAL_TABLE + "("
                + COLUMN_0 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_1 + " TEXT,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " TEXT,"
                + COLUMN_4 + " TEXT,"
                + COLUMN_5 + " INTEGER,"
                + COLUMN_6 + " TEXT,"
                + COLUMN_7 + " TEXT,"
                + COLUMN_8 + " TEXT,"
                + COLUMN_9 + " INTEGER,"
                + COLUMN_10 + " BOOLEAN,"
                + COLUMN_11 + " BOOLEAN,"
                + COLUMN_12 + " BOOLEAN,"
                + COLUMN_13 + " TEXT,"
                + COLUMN_14 + " INTEGER,"
                + COLUMN_15 + " TEXT,"
                + COLUMN_16 + " TEXT,"
                + COLUMN_17 + " TEXT,"
                + COLUMN_18 + " INTEGER" + ")";

        db.execSQL(BEFORE_MEAL_QUERY);
        db.execSQL(AFTER_MEAL_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BEFORE_MEAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AFTER_MEAL_TABLE);
        onCreate(db);
    }

    public void insertData(MedicineModel medicineModel, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_1, medicineModel.getDate());
        values.put(COLUMN_2, medicineModel.getMedicineName());
        values.put(COLUMN_3, medicineModel.getMedicineType());
        values.put(COLUMN_4, medicineModel.getImagePath());
        values.put(COLUMN_5, medicineModel.getNumberOfSlot());
        values.put(COLUMN_6, medicineModel.getFirstSlotTime());
        values.put(COLUMN_7, medicineModel.getSecondSlotTime());
        values.put(COLUMN_8, medicineModel.getThirdSlotTime());
        values.put(COLUMN_9, medicineModel.getNumberOfDays());
        values.put(COLUMN_10, medicineModel.isEveryday());
        values.put(COLUMN_11, medicineModel.isSpecificDaysOfWeek());
        values.put(COLUMN_12, medicineModel.isDaysInterval());
        values.put(COLUMN_13, medicineModel.getDaysNameOfWeek());
        values.put(COLUMN_14, medicineModel.getDaysInterval());
        values.put(COLUMN_15, medicineModel.getStartDate());
        values.put(COLUMN_16, medicineModel.getStatus());
        values.put(COLUMN_17, medicineModel.getMedicineMeal());
        values.put(COLUMN_18, medicineModel.getUniqueCode());

        db.insert(tableName, null, values);
        db.close();

    }

    public MedicineModel getSingleMedicine(String serarchKeyword, String tableName) {
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE ID=?";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{serarchKeyword});

        if (cursor != null)
            cursor.moveToFirst();

        MedicineModel medicineModel = new MedicineModel();

        try {
            medicineModel.setId(Integer.parseInt(cursor.getString(0)));
            medicineModel.setDate(cursor.getString(1));
            medicineModel.setMedicineName(cursor.getString(2));
            medicineModel.setMedicineType(cursor.getString(3));
            medicineModel.setImagePath(cursor.getString(4));
            medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(5)));
            medicineModel.setFirstSlotTime(cursor.getString(6));
            medicineModel.setSecondSlotTime(cursor.getString(7));
            medicineModel.setThirdSlotTime(cursor.getString(8));
            medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(9)));
            medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(10)));
            medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(11)));
            medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(12)));
            medicineModel.setDaysNameOfWeek(cursor.getString(13));
            medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(14)));
            medicineModel.setStartDate(cursor.getString(15));
            medicineModel.setStatus(cursor.getString(16));
            medicineModel.setMedicineMeal(cursor.getString(17));
            medicineModel.setUniqueCode(Integer.parseInt(cursor.getString(18)));

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return medicineModel;
    }

    public List<MedicineModel> getSelectedList(String searchKeyword, String tableName) {
        List<MedicineModel> medicineModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE DATE=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchKeyword});

        if (cursor.moveToFirst()) {
            do {

                MedicineModel medicineModel = new MedicineModel();
                medicineModel.setId(Integer.parseInt(cursor.getString(0)));
                medicineModel.setDate(cursor.getString(1));
                medicineModel.setMedicineName(cursor.getString(2));
                medicineModel.setMedicineType(cursor.getString(3));
                medicineModel.setImagePath(cursor.getString(4));
                medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(5)));
                medicineModel.setFirstSlotTime(cursor.getString(6));
                medicineModel.setSecondSlotTime(cursor.getString(7));
                medicineModel.setThirdSlotTime(cursor.getString(8));
                medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(9)));
                medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(10)));
                medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(11)));
                medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(12)));
                medicineModel.setDaysNameOfWeek(cursor.getString(13));
                medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(14)));
                medicineModel.setStartDate(cursor.getString(15));
                medicineModel.setStatus(cursor.getString(16));
                medicineModel.setMedicineMeal(cursor.getString(17));
                medicineModel.setUniqueCode(Integer.parseInt(cursor.getString(18)));

                medicineModelList.add(medicineModel);
            } while (cursor.moveToNext());

        }
        return medicineModelList;

    }

    public List<MedicineModel> selectWithMultipleQueries (String startDate, String medName, String firstSlot,
                                                          String numberOfSlot, String numberOfDays, String type,
                                                          String tableName){

        List<MedicineModel> medicineModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " +
                "START_DATE LIKE '%" + startDate + "%' AND " +
                "MEDICINE_NAME LIKE '%" + medName + "%' AND "+
                "FIRST_SLOT LIKE '%" + firstSlot + "%' AND " +
                "NO_OF_SLOT LIKE '%" + numberOfSlot + "%' AND " +
                "NUMBER_OF_DAYS LIKE '%" + numberOfDays + "%' AND " +
                "MEDICINE_TYPE LIKE '%" + type + "%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            do {

                MedicineModel medicineModel = new MedicineModel();
                medicineModel.setId(Integer.parseInt(cursor.getString(0)));
                medicineModel.setDate(cursor.getString(1));
                medicineModel.setMedicineName(cursor.getString(2));
                medicineModel.setMedicineType(cursor.getString(3));
                medicineModel.setImagePath(cursor.getString(4));
                medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(5)));
                medicineModel.setFirstSlotTime(cursor.getString(6));
                medicineModel.setSecondSlotTime(cursor.getString(7));
                medicineModel.setThirdSlotTime(cursor.getString(8));
                medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(9)));
                medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(10)));
                medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(11)));
                medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(12)));
                medicineModel.setDaysNameOfWeek(cursor.getString(13));
                medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(14)));
                medicineModel.setStartDate(cursor.getString(15));
                medicineModel.setStatus(cursor.getString(16));
                medicineModel.setMedicineMeal(cursor.getString(17));
                medicineModel.setUniqueCode(Integer.parseInt(cursor.getString(18)));

                medicineModelList.add(medicineModel);

            }while (cursor.moveToNext());
        }

        return medicineModelList;
    }

    public  List<MedicineModel> getAllData(String tableName)
    {

        List<MedicineModel> medicineModelList = new ArrayList<MedicineModel>();

        String selectQuery = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst())
        {
            do {

                MedicineModel medicineModel = new MedicineModel();
                medicineModel.setId(Integer.parseInt(cursor.getString(0)));
                medicineModel.setDate(cursor.getString(1));
                medicineModel.setMedicineName(cursor.getString(2));
                medicineModel.setMedicineType(cursor.getString(3));
                medicineModel.setImagePath(cursor.getString(4));
                medicineModel.setNumberOfSlot(Integer.parseInt(cursor.getString(5)));
                medicineModel.setFirstSlotTime(cursor.getString(6));
                medicineModel.setSecondSlotTime(cursor.getString(7));
                medicineModel.setThirdSlotTime(cursor.getString(8));
                medicineModel.setNumberOfDays(Integer.parseInt(cursor.getString(9)));
                medicineModel.setEveryday(Boolean.parseBoolean(cursor.getString(10)));
                medicineModel.setSpecificDaysOfWeek(Boolean.parseBoolean(cursor.getString(11)));
                medicineModel.setDaysInterval(Boolean.parseBoolean(cursor.getString(12)));
                medicineModel.setDaysNameOfWeek(cursor.getString(13));
                medicineModel.setDaysInterval(Integer.parseInt(cursor.getString(14)));
                medicineModel.setStartDate(cursor.getString(15));
                medicineModel.setStatus(cursor.getString(16));
                medicineModel.setMedicineMeal(cursor.getString(17));
                medicineModel.setUniqueCode(Integer.parseInt(cursor.getString(18)));

                medicineModelList.add(medicineModel);

            }while (cursor.moveToNext());
        }

        return medicineModelList;
    }
    public int updateData (MedicineModel medicineModel, String tableName){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_1, medicineModel.getDate());
        values.put(COLUMN_2, medicineModel.getMedicineName());
        values.put(COLUMN_3, medicineModel.getMedicineType());
        values.put(COLUMN_4, medicineModel.getImagePath());
        values.put(COLUMN_5, medicineModel.getNumberOfSlot());
        values.put(COLUMN_6, medicineModel.getFirstSlotTime());
        values.put(COLUMN_7, medicineModel.getSecondSlotTime());
        values.put(COLUMN_8, medicineModel.getThirdSlotTime());
        values.put(COLUMN_9, medicineModel.getNumberOfDays());
        values.put(COLUMN_10, medicineModel.isEveryday());
        values.put(COLUMN_11, medicineModel.isSpecificDaysOfWeek());
        values.put(COLUMN_12, medicineModel.isDaysInterval());
        values.put(COLUMN_13, medicineModel.getDaysNameOfWeek());
        values.put(COLUMN_14, medicineModel.getDaysInterval());
        values.put(COLUMN_15, medicineModel.getStartDate());
        values.put(COLUMN_16, medicineModel.getStatus());
        values.put(COLUMN_17, medicineModel.getMedicineMeal());
        values.put(COLUMN_18, medicineModel.getUniqueCode());




        return db.update(tableName, values, COLUMN_0 + " = ?",
                new String[] { String.valueOf(medicineModel.getId()) });
    }

    public void deleteData(MedicineModel medicineModel,String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tableName,COLUMN_0 + " = ?",new String[]{String.valueOf(medicineModel.getId())});
        db.close();
    }
}
