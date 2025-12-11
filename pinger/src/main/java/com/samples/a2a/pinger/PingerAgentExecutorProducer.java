package com.samples.a2a.pinger;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import mosaico.acl.ACLMessage;
import mosaico.acl.BDIAgentExecutor;


/**
 * Producer for Pinger Agent Executor.
 */
@ApplicationScoped
public final class PingerAgentExecutorProducer  {

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
    private static class PingerAgentExecutor extends BDIAgentExecutor {

        static final String myUrl = "http://127.0.0.1:9999";
        static final String otherAgentUrl = "http://127.0.0.1:9998";

        @Override
        public void execute(final ACLMessage message,
                            final EventQueue eventQueue) throws JSONRPCError {



            if (message.content().equals("pong")&& message.illocution()!= null && message.illocution().equals("tell")) {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : tell/pong received."));
                System.out.println("Test OK : Received a tell/pong message.");

            }
            else if (message.content().startsWith("do_ping") && message.illocution()!= null && message.illocution().equals("achieve")){
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
                System.out.println("Unknown request (only receive do_ping or pong requests)." );
                System.exit(0);
            }

        }


    }
}
