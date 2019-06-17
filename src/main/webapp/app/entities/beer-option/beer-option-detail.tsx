import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './beer-option.reducer';
import { IBeerOption } from 'app/shared/model/beer-option.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBeerOptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class BeerOptionDetail extends React.Component<IBeerOptionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { beerOptionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            BeerOption [<b>{beerOptionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="amount">Amount</span>
            </dt>
            <dd>{beerOptionEntity.amount}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{beerOptionEntity.name}</dd>
            <dt>
              <span id="brand">Brand</span>
            </dt>
            <dd>{beerOptionEntity.brand}</dd>
            <dt>
              <span id="volume">Volume</span>
            </dt>
            <dd>{beerOptionEntity.volume}</dd>
            <dt>
              <span id="abv">Abv</span>
            </dt>
            <dd>{beerOptionEntity.abv}</dd>
          </dl>
          <Button tag={Link} to="/entity/beer-option" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/beer-option/${beerOptionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ beerOption }: IRootState) => ({
  beerOptionEntity: beerOption.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BeerOptionDetail);
