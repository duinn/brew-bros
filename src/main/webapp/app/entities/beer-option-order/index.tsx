import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BeerOptionOrder from './beer-option-order';
import BeerOptionOrderDetail from './beer-option-order-detail';
import BeerOptionOrderUpdate from './beer-option-order-update';
import BeerOptionOrderDeleteDialog from './beer-option-order-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BeerOptionOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BeerOptionOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BeerOptionOrderDetail} />
      <ErrorBoundaryRoute path={match.url} component={BeerOptionOrder} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={BeerOptionOrderDeleteDialog} />
  </>
);

export default Routes;
