import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order.reducer';
import { IOrder } from 'app/shared/model/order.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrderDetail extends React.Component<IOrderDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { orderEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Order [<b>{orderEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="placedDateTime">Placed Date Time</span>
            </dt>
            <dd>
              <TextFormat value={orderEntity.placedDateTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="deliveryDateTime">Delivery Date Time</span>
            </dt>
            <dd>
              <TextFormat value={orderEntity.deliveryDateTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="orderStatus">Order Status</span>
            </dt>
            <dd>{orderEntity.orderStatus}</dd>
          </dl>
          <Button tag={Link} to="/entity/order" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/order/${orderEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ order }: IRootState) => ({
  orderEntity: order.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrderDetail);
