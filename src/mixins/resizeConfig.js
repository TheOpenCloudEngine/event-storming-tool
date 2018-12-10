import joint from 'jointjs';
import resizeIcon from '@/assets/highlight-shape.svg';

export default {
  props: ['highlighted', 'paper', 'processNode', 'planeElements', 'moddle'],
  data() {
    return {
      anchorPoints: [],
      isResizing: true,
      elementPadding: 5,
    };
  },
  watch: {
    highlighted(highlighted) {
      if (highlighted) {
        this.addResizeAnchors();
      } else {
        this.removeResizeAnchors();
      }
    },
  },
  methods: {
    configureResize() {
      if (!this.resizeConfig) {
        this.resizeConfig = [];
      }

      const point = new joint.shapes.standard.EmbeddedImage();

      this.anchorPoints.push(point);

      point.set('isDrag', true);

      point.attr({
        root: { display: 'none' },
        body: {
          fill: '#fff',
          stroke: ' #fff',
          opacity: 0.8,
          cursor: 'nwse-resize',
        },
        image: {
          xlinkHref: resizeIcon,
          cursor: 'nwse-resize',
          refWidth: 20,
          refHeight: 20,
          resetOffset: true,
        },
      });

      this.shape.embed(point);
      point.addTo(this.graph);

      const { width, height } = this.shape.get('size');
      const { x, y } = this.shape.position();
      point.position(x + width, y + height);
      point.set('previousPosition', point.position());

      this.shape.on('change:size', this.updateAnchorPointPosition);

      point.listenTo(this.paper, 'element:pointerdown', cellView => {
        /* Only listen to position change when dragging the point. */
        if (cellView.model === point) {
          point.on('change:position', this.resizeElement);
          this.shape.off('change:size', this.updateAnchorPointPosition);

          point.listenToOnce(this.paper, 'element:pointerup', () => {
            this.updateAnchorPointPosition();
            point.off('change:position', this.resizeElement);
            this.shape.on('change:size', this.updateAnchorPointPosition);
          });
        }
      });

    },
    resizeElement(point, newPosition) {
      const { x, y } = newPosition;
      const {
        x: poolX,
        y: poolY,
        width: poolWidth,
        height: poolHeight,
      } = this.shape.getBBox();

      if (point.get('previousPosition').x === x && point.get('previousPosition').y === y) {
        return;
      }

      if(this.isResizing) {
        this.shape.resize(Math.max(x - poolX, 300), Math.max(y - poolY, 100));
        if (x < 300 || y < 60) {
          point.position(Math.max(x, 300), Math.max(y, 60), {
            parentRelative: true,
          });
        }
      }

      this.shape.getEmbeddedCells().filter(element => element.component).forEach(element => {
        const {
          y: elementY,
          height: elementHeight,
        } = element.findView(this.paper).getBBox();

        const poolBottomEdge = poolY + poolHeight;
        const elementBottomEdge = elementY + elementHeight;

        if(elementBottomEdge + this.elementPadding > poolBottomEdge) {
          this.isResizing = false;
        }

        if(elementBottomEdge + this.elementPadding < poolBottomEdge) {
          this.isResizing = true;
        }
      });

      if(!this.isResizing) {
        this.shape.resize(Math.max(x - poolX, poolWidth), Math.max(y - poolY, poolHeight), {
          parentRelative: true,
        });
        this.captureChildren();
        this.updateAnchorPointPosition();
      }

      point.set('previousPosition', { x, y });

      if (this.laneSet) {
        this.shape.resize(Math.max(x - poolX, 300), Math.max(y - poolY, 400));
        this.resizeLanes();
      }

      this.updateCrownPosition();
    },
    addResizeAnchors() {
      this.anchorPoints.forEach(button => {
        button.attr('root/display', 'initial');
      });
    },
    removeResizeAnchors() {
      this.anchorPoints.forEach(button => {
        button.attr('root/display', 'none');
      });
    },
    updateAnchorPointPosition() {
      const { x, y, width, height } = this.shape.findView(this.paper).getBBox();

      this.anchorPoints.forEach( point => {
        point.position(x + width, y + height);
      });
    },
  },
  mounted() {
    this.$nextTick(() => {
      /* Use nextTick to ensure this code runs after the component it is mixed into mounts.
       * This will ensure this.shape is defined. */
      this.configureResize();
    });
  },
};
