package com.samples.a2a.pingable;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import mosaico.acl.ACLMessage;
import mosaico.acl.BDIAgentExecutor;
import org.eclipse.microprofile.config.inject.ConfigProperty;


/**
 * Producer for Content Pingable Agent Executor.
 */
@ApplicationScoped
public final class PingableAgentExecutorProducer {
    /** HTTP port for the agent. */
    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    private int httpPort;

    /**
     * Creates the agent executor for the content writer agent.
     *
     * @return the agent executor
     */
    @Produces
    public AgentExecutor agentExecutor() {
        return new PingableAgentExecutor();
    }

    /**
     * Agent executor implementation for ping service.
     */
    private class PingableAgentExecutor extends BDIAgentExecutor {



        final String myUrl = "http://127.0.0.1:" + httpPort ;

        @Override
        public void executeTell(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {
            eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
            System.out.println("Unknown request (only receive achieve/ping requests)." );
            System.exit(-1);
        }

        @Override
        public void executeAchieve(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {
            assert (m.illocution()!= null && m.illocution().equals("achieve")) ;

            if (m.content().equals("ping")) {
                System.out.println("Received a achieve/ping request");
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : achieve/ping received."));

                spawn_send_message(m.sender(), myUrl, "tell", "atom_codec", "pong");
            }
            else {
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
                System.out.println("Unknown request (only receive achieve/ping requests)." );
                System.exit(-1);
            }
        }
        @Override
        public void executeAsk(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
                System.out.println("Unknown request (only receive achieve/ping requests)." );
                System.exit(-1);
        }

        @Override
        public void executeOther(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
                System.out.println("Unknown request (only receive achieve/ping requests)." );
                System.exit(-1);
        }
    }
}
