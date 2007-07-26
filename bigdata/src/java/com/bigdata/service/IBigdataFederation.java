package com.bigdata.service;

import java.util.UUID;

import com.bigdata.btree.IIndex;
import com.bigdata.journal.IIndexManager;
import com.bigdata.journal.IIndexStore;
import com.bigdata.scaleup.MetadataIndex;
import com.bigdata.scaleup.PartitionMetadata;
import com.bigdata.service.DataService.NoSuchIndexException;

/**
 * The client-facing interface to a bigdata federation.
 * 
 * @author <a href="mailto:thompsonbry@users.sourceforge.net">Bryan Thompson</a>
 * @version $Id$
 * 
 * @todo reconcile with {@link IIndexManager} and {@link IIndexStore}.
 */
public interface IBigdataFederation {

    /**
     * A constant that may be used as the transaction identifier when the
     * operation is <em>unisolated</em> (non-transactional).  The value of
     * this constant is ZERO (0L).
     */
    public static final long UNISOLATED = 0L;

    /**
     * Return the metadata service (or a proxy for the metadata service).
     */
    public IMetadataService getMetadataService();
    
    /**
     * Return a read-only view of the index partitions for the named
     * scale-out index.
     * 
     * @param name
     *            The name of the scale-out index.
     * 
     * @return The partitions for that index (keys are byte[] partition
     *         separator keys, values are serialized
     *         {@link PartitionMetadata} objects).
     * 
     * @throws NoSuchIndexException
     */
    public MetadataIndex getMetadataIndex(String name);
    
    /**
     * Register a scale-out index with the federation.
     * 
     * @param name
     *            The index name.
     * 
     * @return The UUID for the scale-out index.
     */
    public UUID registerIndex(String name);
    
    /**
     * Create and statically partition a scale-out index.
     * 
     * @param name
     *            The name of the scale-out index.
     * @param separatorKeys
     *            The array of separator keys. Each separator key is
     *            interpreted as an <em>unsigned byte[]</em>. The first
     *            entry MUST be an empty byte[]. The entries MUST be in
     *            sorted order.
     * @param dataServiceUUIDs
     *            The array of data services onto which each partition
     *            defined by a separator key will be mapped. The #of entries
     *            in this array MUST agree with the #of entries in the
     *            <i>separatorKeys</i> array.
     *            
     * @return The UUID of the scale-out index.
     */
    public UUID registerIndex(String name, byte[][] separatorKeys,
            UUID[] dataServiceUUIDs);

    /**
     * Drop a scale-out index.
     * 
     * @param name
     *            The index name.
     */
    public void dropIndex(String name);
    
    /**
     * Obtain a view on a partitioned index.
     * 
     * @param tx
     *            The transaction identifier or zero(0L) iff the index will
     *            be unisolated.
     * 
     * @param name
     *            The index name.
     * 
     * @return The index or <code>null</code> if the index is not
     *         registered with the {@link MetadataService}.
     */
    public IIndex getIndex(long tx, String name);
    
    /**
     * Return the client object that was used to connect to the federation.
     */
    public IBigdataClient getClient();

    /**
     * Disconnect from the federation.
     */
    public void disconnect();
    
}
