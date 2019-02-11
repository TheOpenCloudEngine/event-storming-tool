/* Expression example */
import bpmnExtension from '@processmaker/processmaker-bpmn-moddle/resources/processmaker.json';

/* Our initial node types to register with our modeler */
import {
  association,
  endEvent,
  exclusiveGateway,
  sequenceFlow,
  startEvent,
  startTimerEvent,
  task,
  scriptTask,
  serviceTask,
  textAnnotation,
  pool,
  poolLane,
} from '@/components/nodes';

const nodeTypes = [
  endEvent,
  task,
  scriptTask,
  serviceTask,
  exclusiveGateway,
  sequenceFlow,
  textAnnotation,
  association,
  pool,
  poolLane,
];

window.ProcessMaker.EventBus.$on('modeler-init', ({ registerNode, registerBpmnExtension })  => {
  // Register start events
  registerNode(startEvent);
  registerNode(startTimerEvent, definition => {
    const eventDefinitions = definition.get('eventDefinitions');
    if (eventDefinitions && eventDefinitions.length && eventDefinitions[0].$type === "bpmn:TimerEventDefinition") {
      return 'processmaker-modeler-start-timer-event';
    }
  });
  /* Register basic node types */
  nodeTypes.forEach(config => registerNode(config));

  /* Add a BPMN extension */
  registerBpmnExtension('pm', bpmnExtension);
});
