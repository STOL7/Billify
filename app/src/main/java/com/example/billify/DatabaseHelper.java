package com.example.billify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


public class DatabaseHelper extends SQLiteOpenHelper
{
    private Context mycontext;
    //Birday_Reminder birday = new Birday_Reminder();
    private static String DATABASE_NAME = "billify.db";
    private static String DATABASE_PATH=null ;
    public SQLiteDatabase myDataBase;

    public ArrayList<String> name=new ArrayList<String>();
    public ArrayList<String> image=new ArrayList<>();
    public ArrayList<String> email=new ArrayList<>();
    public ArrayList<Group> groups=new ArrayList<>();
    public ArrayList<Friend> friends=new ArrayList<>();
    public ArrayList<History> histories=new ArrayList<>();


    private static Calendar startDate, endDate;
    public Friend friend;
    public  Group group;
    public  History history;
    public void createDataBase(){

        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.d("Database", "Exists");
        }
        else
            {
                Log.d("Database doesnot exist", "Exists");

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.

            this.getReadableDatabase();
                this.close();

            try
            {

                copyDataBase();

            }
            catch (IOException e)
            {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {

        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }
        catch(SQLiteException e)
        {

            //database does't exist yet.

        }

        if(checkDB != null)
        {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException
    {

        //Open your local db as the input stream
        InputStream myInput = mycontext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH+DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException
    {

        //Open the database
        String myPath = DATABASE_PATH+DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        myDataBase.close();

    }

    @Override
    public synchronized void close()
    {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.



    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,1);

        DATABASE_PATH= "/data/data/" + context.getPackageName() + "/" + "databases/";
        this.mycontext=context;

    }

    public ArrayList<Friend> geFriend()
    {


     try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="partner";
            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb, null);

            Log.d("branch", "database accessed successfully  123456");

            cursor.moveToFirst();



            while (!cursor.isAfterLast())
            {
               //list.add(cursor.getString(1));
                friend = new Friend();
                friend.setName(cursor.getString(1));
                friend.setProfile(cursor.getString(5));
                friend.setEmail(cursor.getString(3));

                friend.setContact(cursor.getString(2));
                friend.setId(cursor.getString(0));

                friend.setBalance(cursor.getInt(4));

                friends.add(friend);

                Log.d("branch", "database accessed successfully  ");
                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  friends;
    }


    public ArrayList<Group> getGroups()
    {


        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();



            Cursor cursor = databases.rawQuery("SELECT * FROM Group_T", null);

            Log.d("branch", "database accessed successfully  123456");

            cursor.moveToFirst();



            while (!cursor.isAfterLast())
            {
                //list.add(cursor.getString(1));
                group = new Group();
                group.setName(cursor.getString(1));
                group.setImage(cursor.getString(2));
                group.setDescription(cursor.getString(3));

                group.setId(cursor.getString(0));

                group.setDate(cursor.getString(4));

                groups.add(group);

                Log.d("branch", "database accessed successfully  ");
                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  groups;
    }

    public ArrayList<Friend> getGroupMembers(String id)
    {


        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();



            Cursor cursor = databases.rawQuery("SELECT * FROM group_members where group_id = \""+id+"\"", null);

            Log.d("branch", "database accessed successfully  123456");

            cursor.moveToFirst();



            while (!cursor.isAfterLast())
            {
                Cursor cursor1 = databases.rawQuery("SELECT * FROM partner where id = \""+cursor.getString(1)+"\"", null);
                cursor1.moveToFirst();
                friend = new Friend();
                friend.setName(cursor1.getString(1));
                friend.setProfile(cursor1.getString(5));
                friend.setEmail(cursor1.getString(3));

                friend.setContact(cursor1.getString(2));
                friend.setId(cursor1.getString(0));

                friend.setBalance(cursor1.getInt(4));

                friends.add(friend);
                Log.d("branch", "database accessed successfully  ");
                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  friends;
    }

    public ArrayList<History> geHistory()
    {

        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);

        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="History";
            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb, null);

            Log.d("branch", "Friends retrived succefully");

            cursor.moveToFirst();



            while (!cursor.isAfterLast())
            {
                //list.add(cursor.getString(1));
                history = new History();
                history.setAmount(cursor.getLong(1));
                history.setBillIMage(cursor.getString(3));
                history.setDate(cursor.getString(2));

                history.setDescription(cursor.getString(5));
                history.setId(cursor.getString(0));

                history.setTitle(cursor.getString(6));
                history.setCategory(cursor.getString(7));

                history.setGroup_id(cursor.getString(4));
                histories.add(history);

                Log.d("branch", "history retrived succefully ");
                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  histories;
    }

    public ArrayList<history_membor> geHistoryMember(String id)
    {


        ArrayList<history_membor> f = new ArrayList<>();
        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="history_details";
            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where history_id = \""+id+"\"" , null);

            Log.d("branch", "Friends retrived succefully");

            cursor.moveToFirst();





            while (!cursor.isAfterLast())
            {
             history_membor hs = new history_membor();
                hs.setExpense(cursor.getString(4));
                hs.setId(cursor.getString(2));
                hs.setPaid(cursor.getString(3));


                f.add(hs);

                Log.d("branch", "history retrived succefully ");
                cursor.moveToNext();
            }

            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  f;
    }

    public int delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            int r = db.delete("History   ","id=?",new String[]{String.valueOf(id)});
            int x = db.delete("expand_history","history_id=?",new String[]{String.valueOf(id)});
            int y = db.delete("history_details","history_id=?",new String[]{String.valueOf(id)});
            return r;
        }
        catch (Exception ex)
        {
            return  0;
        }

    }
    public ArrayList<friend_history> getFriendForDelete(String id)
    {

        ArrayList<friend_history> f = new ArrayList<>();
        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="expand_history";

            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where history_id = \""+id+"\"" , null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {

                friend_history fs = new friend_history();


                fs.setUser_id(cursor.getString(2));
                fs.setPaid(cursor.getLong(3));
                fs.setOpp_id(cursor.getString(5));
                fs.setHistory_id(cursor.getString(1));
                f.add(fs);


                //Log.d("branch", "history retrived succefully ");
                cursor.moveToNext();
            } databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  f;
    }
    public ArrayList<friend_history> getFriendHistory(String id,String friend_id)
    {

        ArrayList<friend_history> f = new ArrayList<>();
        try
        {


            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="expand_history";

            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where member_id = \""+id+"\" and opposite_id = \""+friend_id+"\"" , null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {

                friend_history fs = new friend_history();


                fs.setUser_id(cursor.getString(2));
                fs.setPaid(cursor.getLong(3));
               // fs.setOpp_id(cursor.getString(5));
                fs.setHistory_id(cursor.getString(1));
                f.add(fs);


                //Log.d("branch", "history retrived succefully ");
                cursor.moveToNext();
            }
             cursor = databases.rawQuery("SELECT * FROM "+tb+" where member_id = \""+friend_id+"\" and opposite_id = \""+id+"\"" , null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {

                friend_history fs = new friend_history();


                fs.setUser_id(cursor.getString(2));
                fs.setPaid(cursor.getLong(3));
                fs.setHistory_id(cursor.getString(1));
                f.add(fs);

                cursor.moveToNext();
            }
            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  f;
    }

    public ArrayList<History> getGroupHistory(String id)
    {


        try
        {

            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="History";

            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where group_id = \""+id+"\"" , null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                history = new History();
                history.setAmount(cursor.getLong(1));
                history.setBillIMage(cursor.getString(3));
                history.setDate(cursor.getString(2));

                history.setDescription(cursor.getString(5));
                history.setId(cursor.getString(0));

                history.setTitle(cursor.getString(6));
                history.setCategory(cursor.getString(7));

                history.setGroup_id(cursor.getString(4));
                histories.add(history);

                cursor.moveToNext();
            }



            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  histories;
    }

    public History getHistory1(String id)
    {

        History hs = new History();;
        try
        {

            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="History";

            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where id = \""+id+"\"" , null);

            cursor.moveToFirst();
            int cnt=0;
            while (!cursor.isAfterLast())
            {
                Log.d("branch"+ cnt++, id);
                hs.setAmount(cursor.getLong(1));
                hs.setBillIMage(cursor.getString(3));
                hs.setDate(cursor.getString(2));

                hs.setDescription(cursor.getString(5));
                hs.setId(cursor.getString(0));

                hs.setTitle(cursor.getString(6));
                hs.setCategory(cursor.getString(7));

                hs.setGroup_id(cursor.getString(4));
                cursor.moveToNext();
            }


            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  hs;
    }

    public String getMemberName(String id)
    {

        String str="";
        try
        {

            SQLiteDatabase databases = this.getReadableDatabase();


            String tb="partner";
            Cursor cursor = databases.rawQuery("SELECT * FROM "+tb+" where Id = \""+id+"\"" , null);
            Log.d("branch", id);
           cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                str = cursor.getString(1);
                cursor.moveToNext();
            }

            databases.close();
            cursor.close();
        }
        catch (SQLException se)
        {
            Log.d("Exception", se.getMessage());
        }

        return  str;
    }

    public  boolean addNew(String id, String name,String email,String contact,int balance,String profile)
    {
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        email = "'"+email+"'";
        ContentValues values = new ContentValues();
        Log.d("instered", "sadsadad");
        values.put("Name", name);
        values.put("Email", email);
        if(id == null) {
            String uuid = UUID.randomUUID().toString();

            values.put("Id", uuid);
            values.put("Balance", 0);
            values.put("Profile", "");
        }
        else
        {
            values.put("Id", id);
            values.put("Balance", balance);
            values.put("Profile", profile);
        }


        values.put("Contact",contact);

        try
        {
            long newRowId = db.insert("partner", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public  boolean addExpense(String id,String dis,String title,String category, int amount,String date,String bill,String gid)
    {

        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put("id",id);

        values.put("amount",amount);

        values.put("date",date);
        values.put("billImage",bill);
        values.put("group_id",gid);
        values.put("category",category);
        values.put("title",title);
        values.put("description",dis);



        try
        {
            long newRowId = db.insert("History", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public  boolean addIndivisual(String id,String his_id, String mem_id,String opp_id, int paid)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",id);
        values.put("history_id",his_id);
        values.put("member_id",mem_id);
        values.put("paid",paid);

        values.put("opposite_id",opp_id);




        try
        {
            long newRowId = db.insert("expand_history", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public  boolean history_details(String id,String his_id, String mem_id, int paid,int expense)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",id);
        values.put("history_id",his_id);
        values.put("member_id",mem_id);
        values.put("paid",paid);

        values.put("expense",expense);



        try
        {
            long newRowId = db.insert("history_details", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public  boolean newGroup(String id,String title, String dis, String date,String image)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",id);
        values.put("name",title);
        values.put("Description",dis);
        values.put("Image",image);
        values.put("date",date);

        try
        {
            long newRowId = db.insert("Group_T", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public  boolean groupMember(String id,String mid)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("group_id",id);
        values.put("member_id",mid);

        try
        {
            long newRowId = db.insert("group_members", null, values);
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }


    }

    public boolean updateExpense(String uid, long expenses)
    {
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("Balance", expenses);
        try
        {
            long newRowId = db.update("partner",values, "Id=?",new String[]{String.valueOf(uid)});
            return  true;
        }

        catch (Exception ex)
        {
            return  false;
        }
    }


    public boolean findByEmail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        email = "'"+email+"'";

        String query = "Select * FROM partner";

        Cursor cursor = db.rawQuery(query, null);
cursor.moveToFirst();
        //db.close();
        if(!cursor.isAfterLast()) {
            Log.d(cursor.getString(3),email);
            if ((cursor.getString(3).equals(email)))
                return true;
            else
                return false;
        }


            return false;

    }

    public boolean findByContact(String contact)
    {
        SQLiteDatabase db = this.getReadableDatabase();


        String query = "Select * FROM partner where Contact = "+contact;
        Cursor cursor = db.rawQuery(query, null);


        if(cursor.getCount() > 0)
            return true;
        else
            return false;

    }








}