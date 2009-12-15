/**
 * 
 */
package net.sf.gilead.core.beanlib.clone;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import net.sf.beanlib.hibernate3.Hibernate3CollectionReplicator;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.replicator.CollectionReplicatorSpi;
import net.sf.gilead.util.CollectionHelper;

/**
 * @author bruno.marchesson
 *
 */
public class CloneCollectionReplicator extends Hibernate3CollectionReplicator {

	//----
	// Factory
	//----
	public static final Factory factory = new Factory();
	
	
    /**
     * Factory for {@link CloneClassBeanReplicator}
     * 
     * @author bruno.marchesson
     */
	private static class Factory implements CollectionReplicatorSpi.Factory {
        private Factory() {}
        
        public CloneCollectionReplicator newCollectionReplicatable(BeanTransformerSpi beanTransformer) {
            return new CloneCollectionReplicator(beanTransformer);
        }
    }
    
    public static CloneCollectionReplicator newCollectionReplicatable(BeanTransformerSpi beanTransformer) {
        return factory.newCollectionReplicatable(beanTransformer);
    }
    
	protected CloneCollectionReplicator(BeanTransformerSpi beanTransformer) {
		super(beanTransformer);
	}

	@SuppressWarnings("unchecked")
	protected <T> T createToInstance(Object from, Class<T> toClass)
    		throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException 
	{
	//	Unmodifiable collection handling	
	//
		if (CollectionHelper.isUnmodifiableCollection(from))
		{
			from = (Collection<T>) CollectionHelper.getUnmodifiableCollection(from);
			toClass = (Class<T>) from.getClass();
		}
		return super.createToInstance(from, toClass);
	}
}
