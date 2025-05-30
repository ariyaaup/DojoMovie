package com.example.dojomovie.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dojomovie.model.Movie
import com.example.dojomovie.model.transaction
import com.example.dojomovie.model.userData

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "user.db", null, 1) {

    var currentLoggedInUser: userData? = null

    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateUser = """
        CREATE TABLE IF NOT EXISTS Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            phoneNumber VARCHAR UNIQUE,
            password VARCHAR
        )
    """.trimIndent()
        db?.execSQL(queryCreateUser)

        val queryCreateMovie = """
        CREATE TABLE IF NOT EXISTS Movies (
            Movie_ID VARCHAR PRIMARY KEY,
            MovieImage VARCHAR,
            MovieName VARCHAR,
            MoviePrice INTEGER
        )
    """.trimIndent()
        db?.execSQL(queryCreateMovie)

        val queryCreateTransaction = """
        CREATE TABLE IF NOT EXISTS Transactions (
            Transaction_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            User_ID INTEGER,
            Movie_ID VARCHAR,
            Quantity INTEGER
        )
    """.trimIndent()
        db?.execSQL(queryCreateTransaction)
    }


    var loggedUser: userData? = null


    fun insertUser(users: userData){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("phoneNumber", users.phoneNumber)
            put("password", users.password)
        }
        Log.d("UserData","data ${users.phoneNumber} ${users.password}" )
        db.insert("Users", null, values)
        db.close()

    }

    fun getUser() : ArrayList<userData> {
        val users = ArrayList<userData>()

        val db = readableDatabase
        val query = "SELECT * FROM Users"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if(cursor.count > 0) {
            do {
                val user = userData()
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                user.phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
                user.password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                users.add(user)
                Log.d("User", user.phoneNumber.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun insertAllMovies(movieList: ArrayList<Movie>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (movie in movieList) {
                val values = ContentValues().apply {
                    put("Movie_ID", movie.movieID)
                    put("MoviePoster", movie.movieImage)
                    put("MovieName", movie.movieName)
                    put("MoviePrice", movie.moviePrice)
                }
                db.insertWithOnConflict("Movies", null, values, SQLiteDatabase.CONFLICT_IGNORE)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Insert error: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun insertTransaction(transaction: transaction) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("Movie_ID", transaction.movie_id)
                put("Quantity", transaction.quantity)
                put("User_ID", transaction.user_id)
            }
            db.insertWithOnConflict("Transactions", null, values, SQLiteDatabase.CONFLICT_IGNORE)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Insert error: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getTransactionHistory(userId: String): List<transaction> {
        val db = readableDatabase
        val transactionHistory = mutableListOf<transaction>()

        val query = "SELECT * FROM Transactions WHERE User_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val transaction = transaction(
                    transaction_id = cursor.getInt(cursor.getColumnIndexOrThrow("Transaction_ID")),
                    movie_id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID")),
                    user_id = cursor.getString(cursor.getColumnIndexOrThrow("User_ID")),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"))
                )
                transactionHistory.add(transaction)
            } while (cursor.moveToNext())
        } else {
            Log.e("DatabaseHelper", "No data found for user ID: $userId")
        }

        cursor.close()
        db.close()
        return transactionHistory
    }



    fun printAllMovies() {
        val db = readableDatabase
        val query = "SELECT * FROM Movies"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("MovieImage"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("MovieName"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))

                Log.d("Data_movie", "ID: $id, Image: $image, Name: $name, Price: $price")
            } while (cursor.moveToNext())
        } else {
            Log.d("Data_movie", "No data found in Movies table.")
        }

        cursor.close()
        db.close()
    }
    fun getAllMovies(): ArrayList<Movie> {
        val movies = ArrayList<Movie>()
        val db = readableDatabase
        val query = "SELECT * FROM Movies"
        val cursor = db.rawQuery(query, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID"))
                    val image = cursor.getString(cursor.getColumnIndexOrThrow("MovieImage"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("MovieName"))
                    val price = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))
                    val movie = Movie(id, image, name, price)
                    movies.add(movie)
                } while (cursor.moveToNext())
            } else {
                Log.d("Data_movie", "No data found in Movies table.")
            }
        } catch (e: Exception) {
            Log.e("Data_movie", "Error while accessing database: ${e.message}")
        } finally {
            cursor.close()
            db.close()
        }

        return movies
    }




    fun getMovieById(movieID: String): Movie? {
        var result: Movie? = null
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT Movie_ID, MovieImage, MovieName, MoviePrice FROM Movies WHERE Movie_ID = ?",
            arrayOf(movieID)
        )
        if (cursor.moveToFirst()) {
            result = Movie(
                movieID = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID")),
                movieImage = cursor.getString(cursor.getColumnIndexOrThrow("MovieImage")),
                movieName = cursor.getString(cursor.getColumnIndexOrThrow("MovieName")),
                moviePrice = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))
            )
        }
        cursor.close()
        db.close()
        return result
    }


    fun getPhoneNumberById(id: String): String? {
        var userPhoneNumber: String? = null

        val db = readableDatabase
        val query = "SELECT phoneNumber FROM Users WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        if (cursor.moveToFirst()) {
            userPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
        }

        cursor.close()
        db.close()

        return userPhoneNumber
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Users")
        db?.execSQL("DROP TABLE IF EXISTS Movies")
        db?.execSQL("DROP TABLE IF EXISTS Transactions")
        onCreate(db)
    }

  /*  fun updateMovieImage() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("MovieImage", "kongzilla")
        }

        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV001")

        // Mupdate table movie
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DatabaseHelper", "Movie poster update failed.")
        }

        db.close()
    }

    fun updateMovieImage1() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("MovieImage", "kongzilla")
        }

        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV001")

        // Mupdate table movie
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DatabaseHelper", "Movie poster update failed.")
        }

        db.close()
    }

    fun updateMovieImage2() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("MovieImage", "kongzilla")
        }

        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV001")

        // Mupdate table movie
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DatabaseHelper", "Movie poster update failed.")
        }

        db.close()
    }*/
}


