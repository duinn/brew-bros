import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './beer-option.reducer';
import { IBeerOption } from 'app/shared/model/beer-option.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IBeerOptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IBeerOptionUpdateState {
  isNew: boolean;
}

export class BeerOptionUpdate extends React.Component<IBeerOptionUpdateProps, IBeerOptionUpdateState> {
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
    if (errors.length === 0) {
      const { beerOptionEntity } = this.props;
      const entity = {
        ...beerOptionEntity,
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
    this.props.history.push('/entity/beer-option');
  };

  render() {
    const { beerOptionEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="brewbrosApp.beerOption.home.createOrEditLabel">Create or edit a BeerOption</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : beerOptionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="beer-option-id">ID</Label>
                    <AvInput id="beer-option-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="amountLabel" for="beer-option-amount">
                    Amount
                  </Label>
                  <AvField
                    id="beer-option-amount"
                    type="string"
                    className="form-control"
                    name="amount"
                    validate={{
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="beer-option-name">
                    Name
                  </Label>
                  <AvField id="beer-option-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="brandLabel" for="beer-option-brand">
                    Brand
                  </Label>
                  <AvField id="beer-option-brand" type="text" name="brand" />
                </AvGroup>
                <AvGroup>
                  <Label id="volumeLabel" for="beer-option-volume">
                    Volume
                  </Label>
                  <AvField id="beer-option-volume" type="string" className="form-control" name="volume" />
                </AvGroup>
                <AvGroup>
                  <Label id="abvLabel" for="beer-option-abv">
                    Abv
                  </Label>
                  <AvField
                    id="beer-option-abv"
                    type="string"
                    className="form-control"
                    name="abv"
                    validate={{
                      min: { value: 0, errorMessage: 'This field should be at least 0.' },
                      max: { value: 100, errorMessage: 'This field cannot be more than 100.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/beer-option" replace color="info">
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
  beerOptionEntity: storeState.beerOption.entity,
  loading: storeState.beerOption.loading,
  updating: storeState.beerOption.updating,
  updateSuccess: storeState.beerOption.updateSuccess
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
)(BeerOptionUpdate);
