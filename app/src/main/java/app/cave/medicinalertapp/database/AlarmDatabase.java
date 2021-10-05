package app.cave.medicinalertapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import app.cave.medicinalertapp.model.AlarmModel;

public class AlarmDatabase extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "alarm_manager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "alarm_table";

    private static final String COLUMN_1 = "ID";
    private static final String COLUMN_2 = "NDT";
    private static final String COLUMN_3 = "NUMBER_OF_SLOT";
    private static final String COLUMN_4 = "FIRST_SLOT_TIME";
    private static final String COLUMN_5 = "SECOND_SLOT_TIME";
    private static final String COLUMN_6 = "THIRD_SLOT_TIME";
    private static final String COLUMN_7 = "FIRST_SLOT_RC";
    private static final String COLUMN_8 = "SECOND_SLOT_RC";
    private static final String COLUMN_9 = "THIRD_SLOT_RC";

    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " INTEGER,"
                + COLUMN_4 + " TEXT,"
                + COLUMN_5 + " TEXT,"
                + COLUMN_6 + " TEXT,"
                + COLUMN_7 + " INTEGER,"
                + COLUMN_8 + " INTEGER,"
                + COLUMN_9 + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_QUERY);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertAlarn(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, alarmModel.getNdt());
        values.put(COLUMN_3, alarmModel.getNumberOfSlot());
        values.put(COLUMN_4, alarmModel.getFirstSlotTime());
        values.put(COLUMN_5, alarmModel.getSecondSlotTime());
        values.put(COLUMN_6, alarmModel.getThirdSlotTime());
        values.put(COLUMN_7, alarmModel.getFirstSlotRequestCode());
        values.put(COLUMN_8, alarmModel.getSecondSlotRequestCode());
        values.put(COLUMN_9, alarmModel.getThirdSlotRequestCode());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, alarmModel.getNdt());
        values.put(COLUMN_3, alarmModel.getNumberOfSlot());
        values.put(COLUMN_4, alarmModel.getFirstSlotTime());
        values.put(COLUMN_5, alarmModel.getSecondSlotTime());
        values.put(COLUMN_6, alarmModel.getThirdSlotTime());
        values.put(COLUMN_7, alarmModel.getFirstSlotRequestCode());
        values.put(COLUMN_8, alarmModel.getSecondSlotRequestCode());
        values.put(COLUMN_9, alarmModel.getThirdSlotRequestCode());

        return db.update(TABLE_NAME, values, COLUMN_1 + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});

    }

    public List<AlarmModel> getAllAlarmList() {
        List<AlarmModel> alarmModels = new ArrayList<>();
        String seletQuery = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(seletQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setId(Integer.parseInt(cursor.getString(1)));
                alarmModel.setNdt(cursor.getColumnName(2));
                alarmModel.setNumberOfSlot(Integer.parseInt(cursor.getString(3)));
                alarmModel.setFirstSlotTime(cursor.getString(4));
                alarmModel.setSecondSlotTime(cursor.getString(5));
                alarmModel.setThirdSlotTime(cursor.getString(6));
                alarmModel.setFirstSlotRequestCode(Integer.parseInt(cursor.getString(7)));
                alarmModel.setSecondSlotRequestCode(Integer.parseInt(cursor.getString(8)));
                alarmModel.setThirdSlotRequestCode(Integer.parseInt(cursor.getString(9)));

                alarmModels.add(alarmModel);

            } while (cursor.moveToNext());
        }
        return alarmModels;
    }

    public void deletAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_1 + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});
        db.close();
    }

    public List<AlarmModel> getSelectedAlarm(String searchKeyword) {

        List<AlarmModel> alarmModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE NDT=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchKeyword});

        if (cursor.moveToFirst()) {

            do {

                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setId(Integer.parseInt(cursor.getString(0)));
                alarmModel.setNdt(cursor.getString(1));
                alarmModel.setNumberOfSlot(Integer.parseInt(cursor.getString(2)));
                alarmModel.setFirstSlotTime(cursor.getString(3));
                alarmModel.setSecondSlotTime(cursor.getString(5));
                alarmModel.setThirdSlotTime(cursor.getString(6));
                alarmModel.setFirstSlotRequestCode(Integer.parseInt(cursor.getString(6)));
                alarmModel.setSecondSlotRequestCode(Integer.parseInt(cursor.getString(7)));
                alarmModel.setThirdSlotRequestCode(Integer.parseInt(cursor.getString(8)));


                alarmModelList.add(alarmModel);

            } while (cursor.moveToNext());
        }


        return alarmModelList;
    }

}
