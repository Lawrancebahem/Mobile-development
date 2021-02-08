package com.example.madlevel3task2

import android.os.Parcel
import android.os.Parcelable

class Portal(var name: String?, var url: String?): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Portal> {
        override fun createFromParcel(parcel: Parcel): Portal {
            return Portal(parcel)
        }

        override fun newArray(size: Int): Array<Portal?> {
            return arrayOfNulls(size)
        }
    }
}