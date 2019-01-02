package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class RealmHelper {
    private static final RealmHelper ourInstance = new RealmHelper();

    static RealmHelper getInstance() {
        return ourInstance;
    }

    private RealmHelper() {
    }

    /**
     * Delete all items of a single class type.
     *
     * @param clazz class to delete from db
     */
    public void deleteAllFromDB(@NonNull final Class clazz) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.where(clazz).findAll().deleteAllFromRealm();
            }
        });
    }

    /**
     * Delete a single object from db
     *
     * @param realmObject realm object to delete
     */
    public void deleteSingleFromDB(final RealmObject realmObject) {
        if (realmObject != null)
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realmObject.deleteFromRealm();
                }
            });
    }

    /**
     * Get item count of class type
     *
     * @param clazz class type
     */
    public int getCount(Class clazz) {
        return (int) Realm.getDefaultInstance().where(clazz).count();
    }


    public void insertOrUpdateObject(final RealmObject object) {
        if (object != null)
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insertOrUpdate(object);
                }
            });
    }

    public void insertObject(final RealmObject object) {
        if (object != null)
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insert(object);
                }
            });
    }

    public void insertOrUpdateObjects(final Collection<? extends RealmObject> objects) {
        if (objects != null && objects.size() > 0)
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insertOrUpdate(objects);
                }
            });
    }

    public void insertObjects(final Collection<? extends RealmObject> objects) {
        if (objects != null && objects.size() > 0)
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insert(objects);
                }
            });
    }

    /**
     * Check if any item exist in db of class type
     *
     * @param clazz class type
     */
    public boolean isDataExists(Class clazz) {
        long count = Realm.getDefaultInstance().where(clazz).count();
        return count > 0;
    }

    /**
     * Copy object from realm
     *
     * @param clazz class type
     * @param ID object id
     */
    public <T extends RealmObject> T copyObject(int ID, Class<T> clazz) {
        T obj = Realm.getDefaultInstance().where(clazz).equalTo("ID", ID).findFirst();
        if (obj != null)
            return Realm.getDefaultInstance().copyFromRealm(obj);
        else
            return null;
    }

    /**
     * Find all items with given class type and copy from realm to return as a list
     *
     * @param clazz class type
     */
    public <T extends RealmObject> List<T> copyAllData(Class<T> clazz) {
        RealmResults<T> results = Realm.getDefaultInstance().where(clazz).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    /**
     * Find all items with given class type and return managed realm results
     *
     * @param clazz class type
     */
    public <T extends RealmObject> RealmResults<T> findAllDataResults(Class<T> clazz) {
        return Realm.getDefaultInstance().where(clazz).findAll();
    }

    /**
     * Check if any item exist in db of class type
     *
     * @param clazz class type
     */
    public <T extends RealmObject> T findFirst(Class<T> clazz) {
        return Realm.getDefaultInstance().where(clazz).findFirst();
    }

    /**
     * Find last item in db, with sorting field name
     *
     * @param clazz class type
     * @param sortField field name to sort
     */
    public <T extends RealmObject> T findLast(Class<T> clazz, String sortField) {
        return Realm.getDefaultInstance().where(clazz).sort(sortField, Sort.DESCENDING).findFirst();
    }

    /**
     * Copy an object from realm
     *
     * @param realmObject object to copy
     */
    public <T extends RealmObject> T copyFromRealm(T realmObject) {
        if (realmObject.isManaged())
            return Realm.getDefaultInstance().copyFromRealm(realmObject);
        else
            return realmObject;
    }
}
