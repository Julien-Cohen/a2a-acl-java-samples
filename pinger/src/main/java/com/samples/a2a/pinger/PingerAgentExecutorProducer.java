package com.samples.a2a.pinger;

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
 * Producer for Pinger Agent Executor.
 */
@ApplicationScoped
public final class PingerAgentExecutorProducer  {

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
        return new PingerAgentExecutor();
    }

    /**
     * Agent executor implementation for content writer.
     */
    private class PingerAgentExecutor extends BDIAgentExecutor {

        final String myUrl = "http://127.0.0.1:" + httpPort;
        static final String otherAgentUrl = "http://127.0.0.1:9998";

        @Override
        public void executeTell(final ACLMessage message,
                            final EventQueue eventQueue) throws JSONRPCError {
            assert(message.illocution()!= null && message.illocution().equals("tell"));
            if (message.content().equals("pong")) {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : tell/pong received."));
                System.out.println("Test OK : Received a tell/pong message.");
            }
            else {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Unknown request."));
                System.out.println(message.content());
                System.out.println("Unknown request (only receive pong for tell messages)." );
                System.exit(0);
            }
        }

        @Override
        public void executeAchieve(final ACLMessage message,
                            final EventQueue eventQueue) throws JSONRPCError {
            assert(message.illocution()!= null && message.illocution().equals("achieve"));
            if (message.content().startsWith("do_ping")){
                System.out.println("Message achieve/do_ping received.");
                System.out.println("(Going to synchronously reply OK.)");
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : achieve/do_ping received."));
                System.out.println("Going to send PING to the pingable agent.");
                BDIAgentExecutor.spawn_send_message(otherAgentUrl, myUrl, "achieve", "atom", "ping");
                System.out.println("(End spawn sending thread.)");
            }
            else {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Unknown request."));
                System.out.println(message.content());
                System.out.println("Unknown request (only receive do_ping with achieve messages)." );
                System.exit(0);
            }
        }

        @Override
        public void executeAsk(final ACLMessage message,
                            final EventQueue eventQueue) throws JSONRPCError {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Unknown request."));
                System.out.println(message.content());
                System.out.println("Unknown request (does not accept ask requests)." );
                System.exit(0);
        }

        @Override
        public void executeOther(final ACLMessage message,
                            final EventQueue eventQueue) throws JSONRPCError {

                eventQueue.enqueueEvent(A2A.toAgentMessage("Unknown illocution: " + message.illocution()));
                System.out.println(message.content());
                System.out.println("Unknown request (illocution " + message.illocution() + ")." );
                System.exit(0);
        }
    }
}
