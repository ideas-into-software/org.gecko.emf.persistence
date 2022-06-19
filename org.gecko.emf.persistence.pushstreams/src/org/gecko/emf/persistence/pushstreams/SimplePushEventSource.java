package org.gecko.emf.persistence.pushstreams;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.persistence.input.InputContext;
import org.osgi.util.pushstream.PushEventConsumer;

/**
 * Default implementation of a simple push event source that does not run asynchronously.
 * @author mark
 * @since 17.06.2022
 */
public abstract class SimplePushEventSource<RESULT> implements PersistencePushEventSource<RESULT> {
	
	private final InputContext<RESULT> context;

	/**
	 * Creates a new instance.
	 */
	public SimplePushEventSource(InputContext<RESULT> context) {
		Objects.requireNonNull(context);
		this.context = context;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.persistence.pushstreams.PersistencePushEventSource#getContext()
	 */
	@Override
	final public InputContext<RESULT> getContext() {
		return context;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.osgi.util.pushstream.PushEventSource#open(org.osgi.util.pushstream.PushEventConsumer)
	 */
	@Override
	public AutoCloseable open(PushEventConsumer<? super EObject> aec) throws Exception {
		PushEventSourceRunnable<RESULT> runnable = createRunnable(context, aec);
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
