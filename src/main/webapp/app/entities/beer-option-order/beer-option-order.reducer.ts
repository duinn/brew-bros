import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBeerOptionOrder, defaultValue } from 'app/shared/model/beer-option-order.model';

export const ACTION_TYPES = {
  FETCH_BEEROPTIONORDER_LIST: 'beerOptionOrder/FETCH_BEEROPTIONORDER_LIST',
  FETCH_BEEROPTIONORDER: 'beerOptionOrder/FETCH_BEEROPTIONORDER',
  CREATE_BEEROPTIONORDER: 'beerOptionOrder/CREATE_BEEROPTIONORDER',
  UPDATE_BEEROPTIONORDER: 'beerOptionOrder/UPDATE_BEEROPTIONORDER',
  DELETE_BEEROPTIONORDER: 'beerOptionOrder/DELETE_BEEROPTIONORDER',
  RESET: 'beerOptionOrder/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBeerOptionOrder>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BeerOptionOrderState = Readonly<typeof initialState>;

// Reducer

export default (state: BeerOptionOrderState = initialState, action): BeerOptionOrderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BEEROPTIONORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BEEROPTIONORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BEEROPTIONORDER):
    case REQUEST(ACTION_TYPES.UPDATE_BEEROPTIONORDER):
    case REQUEST(ACTION_TYPES.DELETE_BEEROPTIONORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BEEROPTIONORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BEEROPTIONORDER):
    case FAILURE(ACTION_TYPES.CREATE_BEEROPTIONORDER):
    case FAILURE(ACTION_TYPES.UPDATE_BEEROPTIONORDER):
    case FAILURE(ACTION_TYPES.DELETE_BEEROPTIONORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEEROPTIONORDER_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEEROPTIONORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BEEROPTIONORDER):
    case SUCCESS(ACTION_TYPES.UPDATE_BEEROPTIONORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BEEROPTIONORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/beer-option-orders';

// Actions

export const getEntities: ICrudGetAllAction<IBeerOptionOrder> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BEEROPTIONORDER_LIST,
    payload: axios.get<IBeerOptionOrder>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBeerOptionOrder> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BEEROPTIONORDER,
    payload: axios.get<IBeerOptionOrder>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBeerOptionOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BEEROPTIONORDER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IBeerOptionOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BEEROPTIONORDER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBeerOptionOrder> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BEEROPTIONORDER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
