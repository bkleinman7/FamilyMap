package kbrent.FMSDesign.Service;

/*
 * Created by kbrent on 02/09/19.
 */

import android.os.Build;
import android.support.annotation.RequiresApi;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;

/**
 * Deletes ALL data from the database, including user accounts, auth tokens, and
 generated person and event data.
 */
public class ClearService {

    public ClearService(){}

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * @return message of an error or that everything was cleared
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean clear() {

        DataBase dataBase = new DataBase();

        try {
            dataBase.clearTables();
            System.out.println("cleared data");

            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
