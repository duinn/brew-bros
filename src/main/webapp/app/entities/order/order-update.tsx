import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './order.reducer';
import { IOrder } from 'app/shared/model/order.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOrderUpdateState {
  isNew: boolean;
}

export class OrderUpdate extends React.Component<IOrderUpdateProps, IOrderUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    values.placedDateTime = convertDateTimeToServer(values.placedDateTime);
    values.deliveryDateTime = convertDateTimeToServer(values.deliveryDateTime);

    if (errors.length === 0) {
      const { orderEntity } = this.props;
      const entity = {
        ...orderEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/order');
  };

  render() {
    const { orderEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="brewbrosApp.order.home.createOrEditLabel">Create or edit a Order</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : orderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="order-id">ID</Label>
                    <AvInput id="order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="placedDateTimeLabel" for="order-placedDateTime">
                    Placed Date Time
                  </Label>
                  <AvInput
                    id="order-placedDateTime"
                    type="datetime-local"
                    className="form-control"
                    name="placedDateTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.orderEntity.placedDateTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="deliveryDateTimeLabel" for="order-deliveryDateTime">
                    Delivery Date Time
                  </Label>
                  <AvInput
                    id="order-deliveryDateTime"
                    type="datetime-local"
                    className="form-control"
                    name="deliveryDateTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.orderEntity.deliveryDateTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="orderStatusLabel" for="order-orderStatus">
                    Order Status
                  </Label>
                  <AvInput
                    id="order-orderStatus"
                    type="select"
                    className="form-control"
                    name="orderStatus"
                    value={(!isNew && orderEntity.orderStatus) || 'DRAFT'}
                  >
                    <option value="DRAFT">DRAFT</option>
                    <option value="PLACED">PLACED</option>
                    <option value="COMPLETE">COMPLETE</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/order" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  orderEntity: storeState.order.entity,
  loading: storeState.order.loading,
  updating: storeState.order.updating,
  updateSuccess: storeState.order.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrderUpdate);
