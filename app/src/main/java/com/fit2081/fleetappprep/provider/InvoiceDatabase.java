package com.fit2081.fleetappprep.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Invoice.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class InvoiceDatabase extends RoomDatabase {
    public static final String INVOICE_DATABASE_NAME = "invoice_database";
    public abstract InvoiceDao invoiceDao();
    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile InvoiceDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static InvoiceDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InvoiceDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    InvoiceDatabase.class, INVOICE_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
