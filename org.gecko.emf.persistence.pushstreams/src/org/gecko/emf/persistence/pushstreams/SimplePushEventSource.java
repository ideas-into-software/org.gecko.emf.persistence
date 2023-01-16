package org.gecko.emf.persistence.pushstreams;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.context.ResultContext;
import org.gecko.emf.persistence.mapping.EObjectMapper;
import org.osgi.util.pushstream.PushEventConsumer;

/**
 * Default implementation of a simple push event source that does not run asynchronously.
 * @author mark
 * @since 17.06.2022
 */
public abstract class SimplePushEventSource<RESULT, MAPPER extends EObjectMapper> implements PersistencePushEventSource<RESULT, MAPPER> {
	
	private final ResultContext<RESULT, MAPPER> context;

	/**
	 * Creates a new instance.
	 */
	public SimplePushEventSource(ResultContext<RESULT, MAPPER> context) {
		Objects.requireNonNull(context);
		this.context = context;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PersistencePushEventSource#getContext()
	 */
	@Override
	final public ResultContext<RESULT, MAPPER> getContext() {
		return context;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.util.pushstream.PushEventSource#open(org.osgi.util.pushstream.PushEventConsumer)
	 */
	@Override
	public AutoCloseable open(PushEventConsumer<? super EObject> aec) throws Exception {
		PushEventSourceRunnable<RESULT, MAPPER> runnable = createRunnable(context, aec);
		try {
			runnable.run();
		} catch (Exception e) {
			runnable.close(aec);
		}
		return ()->{
			if (runnable != null) {
				runnable.close(aec);
			}
		};
	}

}
