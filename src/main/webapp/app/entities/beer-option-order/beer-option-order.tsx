import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './beer-option-order.reducer';
import { IBeerOptionOrder } from 'app/shared/model/beer-option-order.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IBeerOptionOrderProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IBeerOptionOrderState = IPaginationBaseState;

export class BeerOptionOrder extends React.Component<IBeerOptionOrderProps, IBeerOptionOrderState> {
  state: IBeerOptionOrderState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { beerOptionOrderList, match } = this.props;
    return (
      <div>
        <h2 id="beer-option-order-heading">
          Beer Option Orders
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Beer Option Order
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {beerOptionOrderList && beerOptionOrderList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      ID <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('amount')}>
                      Amount <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Order <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Beer Option <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {beerOptionOrderList.map((beerOptionOrder, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${beerOptionOrder.id}`} color="link" size="sm">
                          {beerOptionOrder.id}
                        </Button>
                      </td>
                      <td>{beerOptionOrder.amount}</td>
                      <td>
                        {beerOptionOrder.order ? (
                          <Link to={`order/${beerOptionOrder.order.id}`}>{beerOptionOrder.order.placedDateTime}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>
                        {beerOptionOrder.beerOption ? (
                          <Link to={`beer-option/${beerOptionOrder.beerOption.id}`}>{beerOptionOrder.beerOption.name}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${beerOptionOrder.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${beerOptionOrder.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${beerOptionOrder.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">No Beer Option Orders found</div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ beerOptionOrder }: IRootState) => ({
  beerOptionOrderList: beerOptionOrder.entities,
  totalItems: beerOptionOrder.totalItems,
  links: beerOptionOrder.links,
  entity: beerOptionOrder.entity,
  updateSuccess: beerOptionOrder.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BeerOptionOrder);
