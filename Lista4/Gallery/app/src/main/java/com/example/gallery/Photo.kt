package com.example.gallery

import android.os.Parcel
import android.os.Parcelable

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Photo(var desc: String, var image: String, var rate: Float, var counter: Int, var rating: Float) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.desc)
        dest?.writeString(this.image)
        dest?.writeFloat(this.rate)
        dest?.writeInt(this.counter)
        dest?.writeFloat(this.rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}
