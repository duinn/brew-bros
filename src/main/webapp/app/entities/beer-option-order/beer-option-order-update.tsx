import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { IBeerOption } from 'app/shared/model/beer-option.model';
import { getEntities as getBeerOptions } from 'app/entities/beer-option/beer-option.reducer';
import { getEntity, updateEntity, createEntity, reset } from './beer-option-order.reducer';
import { IBeerOptionOrder } from 'app/shared/model/beer-option-order.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBeerOptionOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IBeerOptionOrderUpdateState {
  isNew: boolean;
  orderId: string;
  beerOptionId: string;
}

export class BeerOptionOrderUpdate extends React.Component<IBeerOptionOrderUpdateProps, IBeerOptionOrderUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      orderId: '0',
      beerOptionId: '0',
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

    this.props.getOrders();
    this.props.getBeerOptions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { beerOptionOrderEntity } = this.props;
      const entity = {
        ...beerOptionOrderEntity,
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
    this.props.history.push('/entity/beer-option-order');
  };

  render() {
    const { beerOptionOrderEntity, orders, beerOptions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="brewbrosApp.beerOptionOrder.home.createOrEditLabel">Create or edit a BeerOptionOrder</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : beerOptionOrderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="beer-option-order-id">ID</Label>
                    <AvInput id="beer-option-order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="amountLabel" for="beer-option-order-amount">
                    Amount
                  </Label>
                  <AvField
                    id="beer-option-order-amount"
                    type="string"
                    className="form-control"
                    name="amount"
                    validate={{
                      min: { value: 1, errorMessage: 'This field should be at least 1.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="beer-option-order-order">Order</Label>
                  <AvInput id="beer-option-order-order" type="select" className="form-control" name="order.id">
                    <option value="" key="0" />
                    {orders
                      ? orders.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.placedDateTime}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="beer-option-order-beerOption">Beer Option</Label>
                  <AvInput id="beer-option-order-beerOption" type="select" className="form-control" name="beerOption.id">
                    <option value="" key="0" />
                    {beerOptions
                      ? beerOptions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/beer-option-order" replace color="info">
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
  orders: storeState.order.entities,
  beerOptions: storeState.beerOption.entities,
  beerOptionOrderEntity: storeState.beerOptionOrder.entity,
  loading: storeState.beerOptionOrder.loading,
  updating: storeState.beerOptionOrder.updating,
  updateSuccess: storeState.beerOptionOrder.updateSuccess
});

const mapDispatchToProps = {
  getOrders,
  getBeerOptions,
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
)(BeerOptionOrderUpdate);
