import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BeerOption from './beer-option';
import BeerOptionDetail from './beer-option-detail';
import BeerOptionUpdate from './beer-option-update';
import BeerOptionDeleteDialog from './beer-option-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BeerOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BeerOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BeerOptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BeerOption} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={BeerOptionDeleteDialog} />
  </>
);

export default Routes;
