package com.samples.a2a.hot_repo_client;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import mosaico.acl.ACLMessage;
import mosaico.acl.BDIAgentExecutor;


/**
 * Hot-Repository Client Agent Executor.
 */
@ApplicationScoped
public final class HotRepoClientAgentExecutorProducer {

    /**
     * Creates the agent executor for the client agent.
     *
     * @return the agent executor
     */
    @Produces
    public AgentExecutor agentExecutor() {
        return new ClientAgentExecutor();
    }

    /**
     * Agent executor implementation for ping service.
     */
    private static class ClientAgentExecutor extends BDIAgentExecutor {

        static final String myUrl = "http://127.0.0.1:9980"; // see also application.properties:quarkus.http.port
        static final String hotRepoUrl = "http://127.0.0.1:9970";

        static final String skillDescr = "mandatory_skill(achieve, do_jump,0)";

        @Override
        public void execute(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {

            System.out.println("Received a message.");
            if (m.content().startsWith("selected") && m.illocution()!= null && m.illocution().equals("tell")) {
                System.out.println("Received a tell/selected message: " + m.content());
                System.out.println("Decoded selection: " + decodeSelection(m.content()));
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : tell/selected received."));

            }
            else if (m.content().startsWith("go") && m.illocution()!= null && m.illocution().equals("achieve")) {
                System.out.println("Received a achieve/go request");
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : achieve/go received."));

                spawn_send_message(hotRepoUrl, myUrl, "ask", "python_agentspeak_codec", "by_skills(["+skillDescr + "|[]])");
            }
            else {
                System.out.println("Unknown request (only receive go or selected messages)." );
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
                
            }

        }
    /** Returns a new string without selected(" at the beginning and ") at the end. */
    static String decodeSelection(String s){
            assert (s.startsWith("selected(") && s.endsWith(")"));
            final String s2 = s.substring("selected(-".length());
            final String s3 = s2.substring(0, s2.length()-2);
            return s3;

    }


    }
}
