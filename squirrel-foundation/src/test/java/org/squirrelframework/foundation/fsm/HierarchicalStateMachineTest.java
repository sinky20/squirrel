package org.squirrelframework.foundation.fsm;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.squirrelframework.foundation.component.SquirrelPostProcessor;
import org.squirrelframework.foundation.component.SquirrelPostProcessorProvider;
import org.squirrelframework.foundation.component.SquirrelProvider;
import org.squirrelframework.foundation.fsm.annotation.EventType;
import org.squirrelframework.foundation.fsm.annotation.State;
import org.squirrelframework.foundation.fsm.annotation.States;
import org.squirrelframework.foundation.fsm.annotation.Transit;
import org.squirrelframework.foundation.fsm.annotation.Transitions;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;
import org.squirrelframework.foundation.fsm.monitor.TransitionExecTimeMonitor;
import org.squirrelframework.foundation.fsm.monitor.TransitionProgressMonitor;
import org.squirrelframework.foundation.util.TypeReference;

public class HierarchicalStateMachineTest {
	
	public enum HState {
		A, A1, A1a, A1a1, A2, A2a, A3, A4, B, B1, B2, B2a, B3, C
	}
	
	public enum HEvent {
		A2B, B2A, @EventType(EventKind.FINISH)Finish, 
		A12A2, A12A3, A12A4, A12A1a, A12A1a1, A1a12A1, A1a2A1a1, A1a12A1a, A32A1, A12B3, A22A2a, 
		B12B2, B22B2a, B22A
	}
	
	@States({
		@State(parent="A", name="A3", entryCallMethod="enterA3", exitCallMethod="leftA3"), 
		@State(parent="A", name="A4", entryCallMethod="enterA4", exitCallMethod="leftA4", isFinal=true), 
		@State(parent="B", name="B3", entryCallMethod="enterB3", exitCallMethod="leftB3"),
		@State(name="C", entryCallMethod="enterC", exitCallMethod="leftC"),
		@State(parent="A1", name="A1a", entryCallMethod="enterA1a", exitCallMethod="leftA1a"),
		@State(parent="A1a", name="A1a1", entryCallMethod="enterA1a1", exitCallMethod="leftA1a1"),
		@State(name="A2", historyType=HistoryType.DEEP),
		@State(parent="A2", name="A2a", entryCallMethod="enterA2a", exitCallMethod="leftA2a"),
		@State(name="B2", historyType=HistoryType.DEEP),
		@State(parent="B2", name="B2a", entryCallMethod="enterB2a", exitCallMethod="leftB2a"),
		})
	@Transitions({
		@Transit(from="A", to="C", on="Finish", callMethod="transitA2C"),
		@Transit(from="A1", to="A3", on="A12A3", callMethod="transitA12A3"), 
		@Transit(from="A1", to="A4", on="A12A4", callMethod="transitA12A4"), 
		@Transit(from="A3", to="A1", on="A32A1", callMethod="transitA32A1"), 
		@Transit(from="A1", to="B3", on="A12B3", callMethod="transitA12B3"), 
		@Transit(from="A1", to="A1a1", on="A12A1a1", callMethod="transitA12A1a1"), 
		@Transit(from="A1a1", to="A1", on="A1a12A1", callMethod="transitA1a12A1"),
		@Transit(from="A1", to="A1a", on="A12A1a", callMethod="transitA12A1a"),
		@Transit(from="A1a", to="A1a1", on="A1a2A1a1", callMethod="transitA1a2A1a1", type=TransitionType.LOCAL),
		@Transit(from="A1a1", to="A1a", on="A1a12A1a", callMethod="transitA1a12A1a", type=TransitionType.LOCAL),
		@Transit(from="A2", to="A2a", on="A22A2a", callMethod="transitA22A2a", type=TransitionType.LOCAL),
		@Transit(from="B2", to="B2a", on="B22B2a", callMethod="transitB22B2a", type=TransitionType.LOCAL),
		})
	static class HierachicalStateMachine extends AbstractStateMachine<HierachicalStateMachine, HState, HEvent, Integer> {
		
		private StringBuilder logger = new StringBuilder();

		protected HierachicalStateMachine(
                ImmutableState<HierachicalStateMachine, HState, HEvent, Integer> initialState,
                Map<HState, ImmutableState<HierachicalStateMachine, HState, HEvent, Integer>> states) {
	        super(initialState, states);
        }

		public void entryA(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryA");
		}
		
		public void exitA(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitA");
		}
		
		public void transitFromAToBOnA2B(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitFromAToBOnA2B");
		}
		
		public void entryA1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryA1");
		}
		
		public void exitA1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitA1");
		}
		
		public void transitFromA1ToA2OnA12A2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitFromA1ToA2OnA12A2");
		}
		
		public void entryA2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryA2");
		}
		
		public void exitA2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitA2");
		}
		
		public void entryB(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryB");
		}
		
		public void exitB(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitB");
		}
		
		public void entryB1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryB1");
		}
		
		public void exitB1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitB1");
		}
		
		public void entryB2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("entryB2");
		}
		
		public void exitB2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("exitB2");
		}
		
		public void transitFromBToAOnB2A(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitFromBToAOnB2A");
		}
		
		public void transitFromB1ToB2OnB12B2(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitFromB1ToB2OnB12B2");
		}
		
		public void transitFromB2ToAOnB22A(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitFromB2ToAOnB22A");
		}
		
		public void enterA3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterA3");
		}
		
		public void leftA3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftA3");
		}
		
		public void transitA12A3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA12A3");
		}
		
		public void transitA32A1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA32A1");
		}
		
		public void enterB3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterB3");
		}
		
		public void leftB3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftB3");
		}
		
		public void transitA12B3(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA12B3");
		}
		
		public void enterA1a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterA1a");
		}
		
		public void leftA1a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftA1a");
		}
		
		public void enterA1a1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterA1a1");
		}
		
		public void leftA1a1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftA1a1");
		}
		
		public void transitA12A1a1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA12A1a1");
		}
		
		public void transitA1a12A1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA1a12A1");
		}
		
		public void transitA1a12A1a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA1a12A1a");
		}
		
		public void transitA12A1a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA12A1a");
		}
		
		public void transitA1a2A1a1(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA1a2A1a1");
		}
		
		public void enterA2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterA2a");
		}
		
		public void leftA2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftA2a");
		}
		
		public void transitA22A2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA22A2a");
		}
		
		public void enterB2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterB2a");
		}
		
		public void leftB2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftB2a");
		}
		
		public void transitB22B2a(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitB22B2a");
		}
		
		public void enterA4(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterA4");
		}
		
		public void leftA4(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftA4");
		}
		
		public void transitA12A4(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA12A4");
		}
		
		public void transitA2C(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("transitA2C");
		}
		
		public void enterC(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("enterC");
		}
		
		public void leftC(HState from, HState to, HEvent event, Integer context) {
			addOptionalDot();
			logger.append("leftC");
		}
		
		private void addOptionalDot() {
			if (logger.length() > 0) {
				logger.append('.');
			}
		}
		
		public String consumeLog() {
			final String result = logger.toString();
			logger = new StringBuilder();
			return result;
		}
		
	}
	
	HierachicalStateMachine stateMachine;
	
	@BeforeClass
	public static void beforeTest() {
		ConverterProvider.INSTANCE.register(HEvent.class, new Converter.EnumConverter<HEvent>(HEvent.class));
        ConverterProvider.INSTANCE.register(HState.class, new Converter.EnumConverter<HState>(HState.class));
        SquirrelPostProcessorProvider.getInstance().register(HierachicalStateMachine.class, 
        		new TypeReference<TransitionExecTimeMonitor<HierachicalStateMachine, HState, HEvent, Integer>>() {});
        SquirrelPostProcessorProvider.getInstance().register(
        		new TypeReference<ActionExecutor<HierachicalStateMachine, HState, HEvent, Integer>>(){}, 
        		new SquirrelPostProcessor<ActionExecutor<HierachicalStateMachine, HState, HEvent, Integer>>() {
			@Override
            public void postProcess(ActionExecutor<HierachicalStateMachine, HState, HEvent, Integer> component) {
				component.addExecActionListener(new TransitionProgressMonitor<HierachicalStateMachine, HState, HEvent, Integer>());
            }
		});
	}
	
	@AfterClass
	public static void afterTest() {
		ConverterProvider.INSTANCE.clearRegistry();
		SquirrelPostProcessorProvider.getInstance().clearRegistry();
	}
	
	@After
	public void teardown() {
		if(stateMachine.getStatus()!=StateMachineStatus.TERMINATED)
			stateMachine.terminate(null);
		System.out.println("-------------------------------------------------");
	}
	
	
	@Before
    public void setup() {
		StateMachineBuilder<HierachicalStateMachine, HState, HEvent, Integer> builder = 
		        StateMachineBuilderFactory.create(HierachicalStateMachine.class, 
		                HState.class, HEvent.class, Integer.class, new Class<?>[0]);
		builder.externalTransition().from(HState.A).to(HState.B).on(HEvent.A2B);
		builder.externalTransition().from(HState.B).to(HState.A).on(HEvent.B2A);
		
		builder.defineSequentialStatesOn(HState.A, HistoryType.DEEP, HState.A1, HState.A2);
		builder.externalTransition().from(HState.A1).to(HState.A2).on(HEvent.A12A2);
		
		builder.defineSequentialStatesOn(HState.B, HistoryType.SHALLOW, HState.B1, HState.B2);
		builder.externalTransition().from(HState.B1).to(HState.B2).on(HEvent.B12B2);
		builder.externalTransition().from(HState.B2).to(HState.A).on(HEvent.B22A);		
		
		stateMachine = builder.newStateMachine(HState.A);
	}
	
	@Test
	public void testBasicHierarchicalState() {
		stateMachine.start(null);
		assertThat(stateMachine.consumeLog(), is(equalTo("entryA.entryA1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
		
		stateMachine.fire(HEvent.A12A2, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.transitFromA1ToA2OnA12A2.entryA2")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A2)));
		
		stateMachine.fire(HEvent.A2B, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA2.exitA.transitFromAToBOnA2B.entryB.entryB1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B1)));
		
		stateMachine.fire(HEvent.B12B2, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitB1.transitFromB1ToB2OnB12B2.entryB2")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B2)));
		
		stateMachine.fire(HEvent.B22A, 1); 
		// enter A2 by history
		assertThat(stateMachine.consumeLog(), is(equalTo("exitB2.exitB.transitFromB2ToAOnB22A.entryA.entryA2")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A2)));
		
		stateMachine.terminate(null);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA2.exitA")));
	}
	
	@Test
	public void testTestEvent() {
	    HState testResult = stateMachine.test(HEvent.A12A2, 1);
	    assertThat(testResult, is(equalTo(HState.A2)));
	    assertThat(stateMachine.consumeLog(), is(equalTo("")));
	    assertThat(stateMachine.getStatus(), is(equalTo(StateMachineStatus.INITIALIZED)));
	    
	    stateMachine.start(null);
        assertThat(stateMachine.consumeLog(), is(equalTo("entryA.entryA1")));
        assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
        
        stateMachine.fire(HEvent.A12A2, 1);
        assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.transitFromA1ToA2OnA12A2.entryA2")));
        assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A2)));
        
        testResult = stateMachine.test(HEvent.A2B, 1);
        assertThat(stateMachine.consumeLog(), is(equalTo("")));
        assertThat(testResult, is(equalTo(HState.B1)));
	}
	
	@Test
	public void testDeclarativeHierarchicalState() {
		stateMachine.fire(HEvent.A12A3, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("entryA.entryA1.exitA1.transitA12A3.enterA3")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A3)));
		
		stateMachine.fire(HEvent.A32A1, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftA3.transitA32A1.entryA1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
	}
	
	@Test
	public void testTransitionBetweenInnerStates() {
		stateMachine.fire(HEvent.A12B3, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("entryA.entryA1.exitA1.exitA.transitA12B3.entryB.enterB3")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B3)));
	}
	
	@Test
	public void testExternalTransitionBetweenParentAndChild() {
		stateMachine.start(null);
		assertThat(stateMachine.consumeLog(), is(equalTo("entryA.entryA1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
		
		stateMachine.fire(HEvent.A12A1a1, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.entryA1.transitA12A1a1.enterA1a.enterA1a1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1a1)));
		
		stateMachine.fire(HEvent.A1a12A1, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftA1a1.leftA1a.exitA1.transitA1a12A1.entryA1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
	}
	
	@Test
	public void testLocalTransitionBetweenParentAndChild() {
		stateMachine.start(null);
		stateMachine.consumeLog();
		
		stateMachine.fire(HEvent.A12A1a, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.entryA1.transitA12A1a.enterA1a")));
		
		stateMachine.fire(HEvent.A1a2A1a1, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("transitA1a2A1a1.enterA1a1")));
		
		stateMachine.fire(HEvent.A1a12A1a, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftA1a1.transitA1a12A1a")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1a)));
	}
	
	@Test
	public void testParentTransition() {
		stateMachine.start(null);
		stateMachine.consumeLog();
		stateMachine.fire(HEvent.A12A1a1, 1);
		stateMachine.consumeLog();
		stateMachine.fire(HEvent.A12B3, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftA1a1.leftA1a.exitA1.exitA.transitA12B3.entryB.enterB3")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B3)));
	}
	
	@Test
	public void testSavedData() {
	    stateMachine.start(null);
	    stateMachine.fire(HEvent.A12A3, 1);
	    stateMachine.fire(HEvent.A32A1, 0);
	    StateMachineData.Reader<HierachicalStateMachine, HState, HEvent, Integer> savedData = 
	            stateMachine.dumpSavedData();
	    stateMachine.terminate(null);
	    
	    assertThat(savedData.currentState(), is(equalTo(HState.A1)));
        assertThat(savedData.initialState(), is(equalTo(HState.A)));
        assertThat(savedData.lastState(), is(equalTo(HState.A3)));
        
        assertThat(savedData.lastActiveChildStateOf(HState.A), is(equalTo(HState.A3)));
	    setup();
	    
	    stateMachine.loadSavedData(savedData);
	    StateMachineData.Reader<HierachicalStateMachine, HState, HEvent, Integer> savedData2 = 
                stateMachine.dumpSavedData();
	    assertThat(savedData2.lastActiveChildStateOf(HState.A), is(equalTo(HState.A3)));
	}
	
	@Test
	public void testDeepHistoryState() {
		stateMachine.start(null);
		stateMachine.consumeLog();
		stateMachine.fire(HEvent.A12A2, 1);
		stateMachine.consumeLog();
		stateMachine.fire(HEvent.A22A2a, 1);
		stateMachine.consumeLog();
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A2a)));
		
		stateMachine.fire(HEvent.A2B, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftA2a.exitA2.exitA.transitFromAToBOnA2B.entryB.entryB1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B1)));
		
		stateMachine.fire(HEvent.B2A, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitB1.exitB.transitFromBToAOnB2A.entryA.entryA2.enterA2a")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A2a)));
	}
	
	@Test
	public void testShallowHistoryState() {
		stateMachine.fire(HEvent.A2B, 1);
		stateMachine.fire(HEvent.B12B2, 1);
		stateMachine.fire(HEvent.B22B2a, 1);
		stateMachine.consumeLog();
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B2a)));
		
		stateMachine.fire(HEvent.B2A, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("leftB2a.exitB2.exitB.transitFromBToAOnB2A.entryA.entryA1")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.A1)));
		
		stateMachine.fire(HEvent.A2B, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.exitA.transitFromAToBOnA2B.entryB.entryB2")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.B2)));
	}
	
	@Test
	public void testNestedFinalState() {
		stateMachine.start(null);
		stateMachine.consumeLog();
		stateMachine.fire(HEvent.A12A4, 1);
		assertThat(stateMachine.consumeLog(), is(equalTo("exitA1.transitA12A4.enterA4.exitA.transitA2C.enterC")));
		assertThat(stateMachine.getCurrentState(), is(equalTo(HState.C)));
	}
	
	@Test
    public void testExportHierarchicalStateMachine() {
        SCXMLVisitor<HierachicalStateMachine, HState, HEvent, Integer> visitor = SquirrelProvider.getInstance().newInstance(
        		new TypeReference<SCXMLVisitor<HierachicalStateMachine, HState, HEvent, Integer>>() {} );
        stateMachine.accept(visitor);
        visitor.convertSCXMLFile("HierarchicalStateMachine", true);
    }
	
	@Test
    public void testExportDotHierarchicalStateMachine() {
        DotVisitor<HierachicalStateMachine, HState, HEvent, Integer> visitor = SquirrelProvider.getInstance().newInstance(
                new TypeReference<DotVisitor<HierachicalStateMachine, HState, HEvent, Integer>>() {} );
        stateMachine.accept(visitor);
        visitor.convertDotFile("HierarchicalStateMachine");
    }
}
