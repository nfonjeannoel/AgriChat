package com.heavyair.agrichat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ChatMessageEntity::class], version = 1)
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao


    companion object{
        @Volatile
        private var INSTANCE: ChatMessageDatabase? = null


        fun getDatabase(context: Context): ChatMessageDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context, ChatMessageDatabase::class.java, "chat_messages_entity")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}